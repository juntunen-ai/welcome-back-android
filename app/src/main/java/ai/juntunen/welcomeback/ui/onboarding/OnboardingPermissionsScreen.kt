package ai.juntunen.welcomeback.ui.onboarding

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.theme.*

@Composable
fun OnboardingPermissionsScreen(onContinue: () -> Unit) {
    val lang = LanguageManager
    val context = LocalContext.current

    var micGranted by remember {
        mutableStateOf(
            context.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }
    var musicGranted by remember {
        val perm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            android.Manifest.permission.READ_MEDIA_AUDIO
        else android.Manifest.permission.READ_EXTERNAL_STORAGE
        mutableStateOf(
            context.checkSelfPermission(perm) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val micLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> micGranted = granted }

    val musicLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> musicGranted = granted }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(64.dp))

        Text(
            text = lang.t("onboarding.permissions.title"),
            color = OnSurface,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = lang.t("onboarding.permissions.subtitle"),
            color = OnSurface.copy(alpha = 0.6f),
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(32.dp))

        // Microphone permission card
        PermissionCard(
            icon = Icons.Filled.Mic,
            iconTint = Color(0xFF4FC3F7),
            title = lang.t("onboarding.permissions.mic.title"),
            subtitle = lang.t("onboarding.permissions.mic.subtitle"),
            isGranted = micGranted,
            onTap = {
                if (!micGranted) {
                    micLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        // Music library permission card
        val musicPerm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            android.Manifest.permission.READ_MEDIA_AUDIO
        else android.Manifest.permission.READ_EXTERNAL_STORAGE

        PermissionCard(
            icon = Icons.Filled.MusicNote,
            iconTint = Color(0xFF81C784),
            title = lang.t("onboarding.permissions.music.title"),
            subtitle = lang.t("onboarding.permissions.music.subtitle"),
            isGranted = musicGranted,
            onTap = {
                if (!musicGranted) musicLauncher.launch(musicPerm)
            }
        )

        Spacer(Modifier.weight(1f))

        // Continue button
        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentYellow, contentColor = Color.Black),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(lang.t("common.continue"), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun PermissionCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    isGranted: Boolean,
    onTap: () -> Unit
) {
    val lang = LanguageManager
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable { onTap() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
        }

        Spacer(Modifier.width(14.dp))

        Column(Modifier.weight(1f)) {
            Text(title, color = OnSurface, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, color = OnSurface.copy(0.55f), fontSize = 12.sp, lineHeight = 17.sp)
        }

        Spacer(Modifier.width(12.dp))

        // Status chip
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraSmall)
                .background(
                    if (isGranted) AccentYellow.copy(alpha = 0.18f)
                    else OnSurface.copy(alpha = 0.12f)
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (isGranted) lang.t("onboarding.permissions.granted")
                else lang.t("onboarding.permissions.tap_to_grant"),
                color = if (isGranted) AccentYellow else OnSurface.copy(0.55f),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
