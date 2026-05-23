package ai.juntunen.welcomeback.ui.onboarding

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.theme.AccentYellow
import ai.juntunen.welcomeback.ui.theme.OnSurface
import ai.juntunen.welcomeback.ui.theme.SurfaceVariant

@Composable
fun OnboardingCompleteScreen(onDone: () -> Unit) {
    val lang = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsState()

    // Animated checkmark scale-in
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.3f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "check-scale"
    )

    val greeting = if (profile.name.isBlank()) "" else ", ${profile.name}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Animated checkmark in yellow halo
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(AccentYellow.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "✓", fontSize = 88.sp, color = AccentYellow, modifier = Modifier.scale(scale))
        }
        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = lang.t("onboarding.complete.title", greeting),
            color = OnSurface,
            fontSize = 34.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = lang.t("onboarding.complete.subtitle"),
            color = OnSurface.copy(alpha = 0.65f),
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Tips card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(SurfaceVariant.copy(alpha = 0.35f))
                .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            TipRow(icon = "🎤", color = AccentYellow, text = lang.t("onboarding.complete.tip1"))
            Spacer(modifier = Modifier.height(14.dp))
            TipRow(icon = "👨‍👩‍👧", color = Color(0xFF34C759), text = lang.t("onboarding.complete.tip2"))
            Spacer(modifier = Modifier.height(14.dp))
            TipRow(icon = "📚", color = Color(0xFF4D9AFF), text = lang.t("onboarding.complete.tip3"))
        }

        Spacer(modifier = Modifier.weight(1f))

        OnboardingPrimaryButton(
            text = lang.t("onboarding.complete.cta"),
            onClick = {
                appVM.completeOnboarding()
                onDone()
            }
        )
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun TipRow(icon: String, color: Color, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = text,
            color = OnSurface.copy(alpha = 0.75f),
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}
