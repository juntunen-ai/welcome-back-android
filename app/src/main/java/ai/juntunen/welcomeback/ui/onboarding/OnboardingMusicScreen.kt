package ai.juntunen.welcomeback.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.theme.AccentYellow
import ai.juntunen.welcomeback.ui.theme.OnSurface
import ai.juntunen.welcomeback.ui.theme.SurfaceVariant

/**
 * Music step. Spotify SDK is wired in v0.5; for now we explain the
 * intent and advance. The Connect button currently behaves as Skip.
 */
@Composable
fun OnboardingMusicScreen(onContinue: () -> Unit) {
    val lang = LanguageManager

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(AccentYellow.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🎵", fontSize = 56.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = lang.t("onboarding.music.title"),
            color = OnSurface,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = lang.t("onboarding.music.subtitle"),
            color = OnSurface.copy(alpha = 0.65f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // Coming-soon note
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceVariant.copy(alpha = 0.4f))
                .padding(14.dp)
        ) {
            Text(
                text = lang.t("onboarding.music.note"),
                color = OnSurface.copy(alpha = 0.5f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        OnboardingPrimaryButton(text = lang.t("onboarding.music.connect"), onClick = onContinue)
        OnboardingSkipLink(text = lang.t("onboarding.music.skip"), onClick = onContinue)
        Spacer(modifier = Modifier.height(40.dp))
    }
}
