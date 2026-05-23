package ai.juntunen.welcomeback.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.theme.AccentYellow
import ai.juntunen.welcomeback.ui.theme.OnSurface
import ai.juntunen.welcomeback.ui.theme.SurfaceVariant

/**
 * Stub for the on-device AI model download step. Real MediaPipe + Gemma
 * download is scheduled for v0.3; this screen explains why and advances.
 */
@Composable
fun OnboardingModelDownloadScreen(onContinue: () -> Unit) {
    val lang = LanguageManager

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Brain icon in yellow halo
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(AccentYellow.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🧠", fontSize = 58.sp)
        }
        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = lang.t("onboarding.model.title"),
            color = OnSurface,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = lang.t("onboarding.model.subtitle"),
            color = OnSurface.copy(alpha = 0.6f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Coming soon card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(SurfaceVariant.copy(alpha = 0.4f))
                .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Text(
                text = lang.t("onboarding.model.coming_soon"),
                color = OnSurface.copy(alpha = 0.7f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        OnboardingPrimaryButton(
            text = lang.t("common.continue"),
            onClick = onContinue
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}
