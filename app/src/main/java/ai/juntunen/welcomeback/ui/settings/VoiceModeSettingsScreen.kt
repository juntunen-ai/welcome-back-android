package ai.juntunen.welcomeback.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.VoiceMode
import ai.juntunen.welcomeback.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceModeSettingsScreen(navController: NavController) {
    val lang    = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(lang.t("settings.voice.title"), color = OnSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = OnSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            SettingsGroup {
                // Cloud mode
                VoiceModeRow(
                    title = lang.t("settings.voice.cloud"),
                    subtitle = lang.t("settings.voice.cloud.subtitle"),
                    icon = Icons.Filled.Cloud,
                    iconTint = Color(0xFF80DEEA),
                    isSelected = profile.preferredVoiceMode == VoiceMode.CLOUD,
                    onClick = { appVM.updatePreferredVoiceMode(VoiceMode.CLOUD) }
                )
                SettingsDivider()
                // On-device mode
                VoiceModeRow(
                    title = lang.t("settings.voice.local"),
                    subtitle = lang.t("settings.voice.local.subtitle"),
                    icon = Icons.Filled.PhoneAndroid,
                    iconTint = Color(0xFFA5D6A7),
                    isSelected = profile.preferredVoiceMode == VoiceMode.LOCAL,
                    onClick = { appVM.updatePreferredVoiceMode(VoiceMode.LOCAL) }
                )
            }

            Spacer(Modifier.height(20.dp))
            Text(
                text = lang.t("settings.voice.note"),
                color = OnSurface.copy(alpha = 0.4f),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
private fun VoiceModeRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(32.dp).clip(MaterialTheme.shapes.small)
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = OnSurface, fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal)
            Text(subtitle, color = OnSurface.copy(alpha = 0.45f), fontSize = 12.sp)
        }
        if (isSelected) {
            Icon(Icons.Filled.Check, contentDescription = null,
                tint = AccentYellow, modifier = Modifier.size(20.dp))
        }
    }
}
