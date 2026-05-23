package ai.juntunen.welcomeback.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.theme.AccentYellow
import ai.juntunen.welcomeback.ui.theme.OnSurface

@Composable
fun OnboardingProfileScreen(onContinue: () -> Unit) {
    val lang = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsState()

    var name by remember { mutableStateOf(profile.name) }
    val canContinue = name.trim().isNotEmpty()
    val maxLength = 50

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(AccentYellow.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "👤", fontSize = 44.sp)
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = lang.t("onboarding.profile.title"),
            color = OnSurface,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = lang.t("onboarding.profile.subtitle"),
            color = OnSurface.copy(alpha = 0.55f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            OnboardingLabel(lang.t("onboarding.profile.name.label"))
            Spacer(modifier = Modifier.height(8.dp))
            OnboardingTextField(
                value = name,
                onValueChange = { if (it.length <= maxLength) name = it },
                placeholder = lang.t("onboarding.profile.name.placeholder"),
                capitalization = KeyboardCapitalization.Words
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        OnboardingPrimaryButton(
            text = lang.t("onboarding.profile.continue"),
            enabled = canContinue,
            onClick = {
                appVM.updateName(name.trim())
                onContinue()
            }
        )
        Spacer(modifier = Modifier.height(48.dp))
    }
}
