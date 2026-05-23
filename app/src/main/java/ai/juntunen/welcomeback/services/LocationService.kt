package ai.juntunen.welcomeback.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

/**
 * One-shot location + reverse-geocoding service.
 * Mirrors iOS `HomeLocationManager` — requests a single location fix, geocodes it,
 * then stops. Does nothing if permission is not granted.
 */
class LocationService(private val context: Context) {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    private val _city         = MutableStateFlow<String?>(null)
    private val _streetAddress = MutableStateFlow<String?>(null)
    private val _coordinate   = MutableStateFlow<Pair<Double, Double>?>(null)
    private val _isLoading    = MutableStateFlow(false)

    val city:          StateFlow<String?>              = _city.asStateFlow()
    val streetAddress: StateFlow<String?>              = _streetAddress.asStateFlow()
    val coordinate:    StateFlow<Pair<Double, Double>?> = _coordinate.asStateFlow()
    val isLoading:     StateFlow<Boolean>              = _isLoading.asStateFlow()

    fun hasPermission(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

    suspend fun fetchOnce() {
        if (!hasPermission()) return
        _isLoading.value = true
        try {
            val location = getCurrentLocation() ?: return
            _coordinate.value = Pair(location.latitude, location.longitude)
            reverseGeocode(location.latitude, location.longitude)
        } finally {
            _isLoading.value = false
        }
    }

    @Suppress("MissingPermission")
    private suspend fun getCurrentLocation(): Location? =
        suspendCancellableCoroutine { cont ->
            val cts = CancellationTokenSource()
            fusedClient
                .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cts.token)
                .addOnSuccessListener { loc -> if (cont.isActive) cont.resume(loc) }
                .addOnFailureListener { if (cont.isActive) cont.resume(null) }
            cont.invokeOnCancellation { cts.cancel() }
        }

    @Suppress("DEPRECATION")
    private suspend fun reverseGeocode(lat: Double, lon: Double) {
        try {
            if (Build.VERSION.SDK_INT >= 33) {
                suspendCancellableCoroutine { cont ->
                    Geocoder(context, Locale.getDefault()).getFromLocation(lat, lon, 1) { addresses ->
                        val addr = addresses.firstOrNull()
                        _city.value = addr?.locality ?: addr?.adminArea
                        _streetAddress.value = buildStreetString(addr)
                        if (cont.isActive) cont.resume(Unit)
                    }
                }
            } else {
                val addresses = Geocoder(context, Locale.getDefault()).getFromLocation(lat, lon, 1)
                val addr = addresses?.firstOrNull()
                _city.value = addr?.locality ?: addr?.adminArea
                _streetAddress.value = buildStreetString(addr)
            }
        } catch (_: Exception) { /* location display is best-effort */ }
    }

    private fun buildStreetString(addr: android.location.Address?): String? {
        if (addr == null) return null
        val parts = listOfNotNull(
            addr.thoroughfare,
            addr.subThoroughfare?.let { "$it ${addr.thoroughfare ?: ""}" }?.trim()
                ?.takeIf { addr.thoroughfare == null },
            addr.locality
        )
        return parts.firstOrNull()
    }
}
