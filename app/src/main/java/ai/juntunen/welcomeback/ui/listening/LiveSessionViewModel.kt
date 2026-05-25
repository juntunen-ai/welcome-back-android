package ai.juntunen.welcomeback.ui.listening

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ai.juntunen.welcomeback.data.UserProfile
import ai.juntunen.welcomeback.services.GeminiLiveService
import ai.juntunen.welcomeback.services.LiveSessionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Drives the Gemini Live (full-duplex WebSocket) voice session.
 * Falls back to text-based VoiceSession when the live connection fails.
 */
class LiveSessionViewModel(app: Application) : AndroidViewModel(app) {

    private var service: GeminiLiveService? = null

    private val _state = MutableStateFlow<LiveSessionState>(LiveSessionState.Idle)
    val state: StateFlow<LiveSessionState> = _state.asStateFlow()

    private val _useFallback = MutableStateFlow(false)
    val useFallback: StateFlow<Boolean> = _useFallback.asStateFlow()

    fun beginSession(profile: UserProfile) {
        val svc = GeminiLiveService(getApplication())
        service = svc

        // Mirror service state into our state flow
        viewModelScope.launch {
            svc.state.collect { _state.value = it }
        }

        viewModelScope.launch {
            try {
                svc.startSession(profile)
            } catch (e: Exception) {
                _useFallback.value = true
            }
        }
    }

    fun endSession() {
        service?.endSession()
        service = null
    }

    override fun onCleared() {
        super.onCleared()
        endSession()
    }
}
