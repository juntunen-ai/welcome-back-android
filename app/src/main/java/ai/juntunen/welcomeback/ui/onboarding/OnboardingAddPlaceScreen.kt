package ai.juntunen.welcomeback.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.Place
import ai.juntunen.welcomeback.services.GeminiService
import ai.juntunen.welcomeback.ui.theme.AccentYellow
import ai.juntunen.welcomeback.ui.theme.OnSurface
import ai.juntunen.welcomeback.ui.theme.SurfaceVariant

@Composable
fun OnboardingAddPlaceScreen(onContinue: () -> Unit) {
    val lang = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsState()
    val scope = rememberCoroutineScope()

    var placeName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    var aiError by remember { mutableStateOf<String?>(null) }

    val canSave = placeName.trim().isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp)
    ) {
        Spacer(modifier = Modifier.height(56.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(AccentYellow.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📍", fontSize = 38.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = lang.t("onboarding.place.title"),
                color = OnSurface,
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = lang.t("onboarding.place.subtitle"),
                color = OnSurface.copy(alpha = 0.6f),
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        OnboardingLabel(lang.t("onboarding.place.name.label"))
        Spacer(modifier = Modifier.height(8.dp))
        OnboardingTextField(
            value = placeName,
            onValueChange = { placeName = it },
            placeholder = lang.t("onboarding.place.name.placeholder")
        )
        Spacer(modifier = Modifier.height(20.dp))

        OnboardingLabel(lang.t("onboarding.place.desc.label"))
        Spacer(modifier = Modifier.height(8.dp))
        OnboardingTextEditor(
            value = description,
            onValueChange = { description = it },
            placeholder = lang.t("onboarding.place.desc.placeholder"),
            minLines = 5
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "✦ " + lang.t("onboarding.family.ai.hint"),
            color = OnSurface.copy(alpha = 0.4f),
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (description.isNotBlank() && !isGenerating) {
                    isGenerating = true
                    aiError = null
                    scope.launch {
                        try {
                            description = GeminiService.expandMemory(
                                hint = description,
                                userName = profile.name,
                                language = lang.language
                            )
                        } catch (_: Throwable) {
                            aiError = lang.t("onboarding.ai.error")
                        } finally {
                            isGenerating = false
                        }
                    }
                }
            },
            enabled = description.isNotBlank() && !isGenerating,
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentYellow,
                contentColor = Color.Black,
                disabledContainerColor = SurfaceVariant,
                disabledContentColor = OnSurface.copy(alpha = 0.3f),
            ),
            shape = RoundedCornerShape(24.dp),
        ) {
            if (isGenerating) {
                CircularProgressIndicator(color = Color.Black, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = lang.t("onboarding.place.ai.generating"), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            } else {
                Text(text = lang.t("onboarding.place.ai.button"), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        if (aiError != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = aiError!!, color = Color(0xFFFFA000), fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        OnboardingPrimaryButton(
            text = lang.t("onboarding.place.continue"),
            enabled = canSave,
            onClick = {
                appVM.addPlace(Place(name = placeName.trim(), description = description))
                onContinue()
            }
        )
        OnboardingSkipLink(text = lang.t("onboarding.place.skip"), onClick = onContinue)
        Spacer(modifier = Modifier.height(48.dp))
    }
}
