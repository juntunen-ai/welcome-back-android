package ai.juntunen.welcomeback.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.theme.*

private data class LicenseEntry(
    val library: String,
    val license: String,
    val author: String,
    val url: String
)

private val LICENSES = listOf(
    LicenseEntry("Jetpack Compose & AndroidX", "Apache License 2.0", "Google LLC", "https://developer.android.com/jetpack"),
    LicenseEntry("Coil", "Apache License 2.0", "Coil Contributors", "https://coil-kt.github.io/coil/"),
    LicenseEntry("OkHttp", "Apache License 2.0", "Square, Inc.", "https://square.github.io/okhttp/"),
    LicenseEntry("Kotlin", "Apache License 2.0", "JetBrains s.r.o.", "https://kotlinlang.org"),
    LicenseEntry("Material Design 3", "Apache License 2.0", "Google LLC", "https://m3.material.io"),
    LicenseEntry("Ktor", "Apache License 2.0", "JetBrains s.r.o.", "https://ktor.io"),
    LicenseEntry("kotlinx.serialization", "Apache License 2.0", "JetBrains s.r.o.", "https://github.com/Kotlin/kotlinx.serialization"),
    LicenseEntry("Google Maps Compose", "Apache License 2.0", "Google LLC", "https://github.com/googlemaps/android-maps-compose"),
    LicenseEntry("AndroidX DataStore", "Apache License 2.0", "Google LLC", "https://developer.android.com/jetpack/androidx/releases/datastore"),
    LicenseEntry("AndroidX Room", "Apache License 2.0", "Google LLC", "https://developer.android.com/training/data-storage/room"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicensesScreen(navController: NavController) {
    val lang = LanguageManager

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        lang.t("settings.licenses"),
                        color = OnSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = OnSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(LICENSES) { entry ->
                LicenseCard(entry)
            }
        }
    }
}

@Composable
private fun LicenseCard(entry: LicenseEntry) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = entry.library,
                color = OnSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = entry.license,
                color = AccentYellow,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = entry.author,
                color = OnSurface.copy(alpha = 0.60f),
                fontSize = 12.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = entry.url,
                color = OnSurface.copy(alpha = 0.40f),
                fontSize = 11.sp
            )
        }
    }
}
