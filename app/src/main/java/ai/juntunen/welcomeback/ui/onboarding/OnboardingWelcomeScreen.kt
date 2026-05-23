package ai.juntunen.welcomeback.ui.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.theme.AccentYellow
import ai.juntunen.welcomeback.ui.theme.OnSurface

@Composable
fun OnboardingWelcomeScreen(onContinue: () -> Unit) {
    val lang = LanguageManager
    val transition = rememberInfiniteTransition(label = "pulse")
    val scale by transition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(2200), RepeatMode.Reverse),
        label = "icon-scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Pulsing yellow heart
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(AccentYellow.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = AccentYellow,
                modifier = Modifier.size(96.dp).scale(scale)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = lang.t("onboarding.welcome.title"),
            color = OnSurface,
            fontSize = 42.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = lang.t("onboarding.welcome.subtitle"),
            color = OnSurface.copy(alpha = 0.7f),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onContinue,
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentYellow,
                contentColor   = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = lang.t("onboarding.welcome.cta"),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = lang.t("onboarding.welcome.time"),
            color = OnSurface.copy(alpha = 0.35f),
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}
