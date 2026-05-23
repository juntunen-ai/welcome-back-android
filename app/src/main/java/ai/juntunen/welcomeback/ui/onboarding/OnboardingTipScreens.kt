package ai.juntunen.welcomeback.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import ai.juntunen.welcomeback.ui.theme.OnSurface

/**
 * Shared layout for the three "you can add more later" tip screens.
 * Different icon + accent colour per content type.
 */
@Composable
private fun TipScreen(
    icon: String,
    iconColor: Color,
    title: String,
    body: String,
    location: String,
    cta: String,
    onContinue: () -> Unit,
) {
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
                .background(iconColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 52.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = title,
            color = OnSurface,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = body,
            color = OnSurface.copy(alpha = 0.65f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        OnboardingSettingsPill(label = location)
        Spacer(modifier = Modifier.height(32.dp))

        OnboardingPrimaryButton(text = cta, onClick = onContinue)
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun OnboardingFamilyTipScreen(onContinue: () -> Unit) {
    val lang = LanguageManager
    TipScreen(
        icon = "👨‍👩‍👧‍👦",
        iconColor = Color(0xFF34C759), // green
        title = lang.t("onboarding.family.tip.title"),
        body = lang.t("onboarding.family.tip.body"),
        location = lang.t("onboarding.family.tip.location"),
        cta = lang.t("onboarding.family.tip.continue"),
        onContinue = onContinue,
    )
}

@Composable
fun OnboardingPlaceTipScreen(onContinue: () -> Unit) {
    val lang = LanguageManager
    TipScreen(
        icon = "🗺️",
        iconColor = Color(0xFF4D9AFF), // blue
        title = lang.t("onboarding.place.tip.title"),
        body = lang.t("onboarding.place.tip.body"),
        location = lang.t("onboarding.place.tip.location"),
        cta = lang.t("onboarding.place.tip.continue"),
        onContinue = onContinue,
    )
}

@Composable
fun OnboardingStoryTipScreen(onContinue: () -> Unit) {
    val lang = LanguageManager
    TipScreen(
        icon = "📚",
        iconColor = Color(0xFFAF6BE6), // purple
        title = lang.t("onboarding.story.tip.title"),
        body = lang.t("onboarding.story.tip.body"),
        location = lang.t("onboarding.story.tip.location"),
        cta = lang.t("onboarding.story.tip.continue"),
        onContinue = onContinue,
    )
}
