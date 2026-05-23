package ai.juntunen.welcomeback.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.theme.AccentYellow
import ai.juntunen.welcomeback.ui.theme.BackgroundDark
import ai.juntunen.welcomeback.ui.theme.OnSurface
import ai.juntunen.welcomeback.ui.theme.SurfaceVariant

/**
 * Placeholder home screen shown after onboarding completes.
 * Full app (Home, Memories, Family, Settings tabs) arrives in v0.4.
 */
@Composable
fun MainAppPlaceholderScreen(onReset: () -> Unit = {}) {
    val lang = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsState()

    val greeting = if (profile.name.isBlank()) "👋" else "👋 ${profile.name}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(AccentYellow.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🏠", fontSize = 52.sp)
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = greeting,
            color = OnSurface,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Welcome Back",
            color = AccentYellow,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceVariant.copy(alpha = 0.35f))
                .padding(20.dp)
        ) {
            Text(
                text = "The full app experience — conversations, memories, family, and music — is coming in v0.4. Your onboarding data has been saved.",
                color = OnSurface.copy(alpha = 0.65f),
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Dev reset (disappears in prod eventually)
        TextButton(onClick = {
            appVM.resetToNewUser()
            onReset()
        }) {
            Text(
                text = "↩ Reset onboarding (dev)",
                color = OnSurface.copy(alpha = 0.35f),
                fontSize = 13.sp
            )
        }
    }
}
