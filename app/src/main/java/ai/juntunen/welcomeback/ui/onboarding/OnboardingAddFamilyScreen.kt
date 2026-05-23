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
import ai.juntunen.welcomeback.data.FamilyMember
import ai.juntunen.welcomeback.services.GeminiService
import ai.juntunen.welcomeback.ui.theme.AccentYellow
import ai.juntunen.welcomeback.ui.theme.OnSurface
import ai.juntunen.welcomeback.ui.theme.SurfaceVariant

@Composable
fun OnboardingAddFamilyScreen(onContinue: () -> Unit) {
    val lang = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsState()
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("") }
    var memory by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    var aiError by remember { mutableStateOf<String?>(null) }

    val canSave = name.trim().isNotEmpty() && relationship.trim().isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp)
    ) {
        Spacer(modifier = Modifier.height(56.dp))

        // Header
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
                Text(text = "👨‍👩‍👧", fontSize = 38.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = lang.t("onboarding.family.title"),
                color = OnSurface,
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = lang.t("onboarding.family.subtitle"),
                color = OnSurface.copy(alpha = 0.6f),
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Name
        OnboardingLabel(lang.t("onboarding.family.name.label"))
        Spacer(modifier = Modifier.height(8.dp))
        OnboardingTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = lang.t("onboarding.family.name.placeholder"),
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Relationship
        OnboardingLabel(lang.t("onboarding.family.rel.label"))
        Spacer(modifier = Modifier.height(8.dp))
        OnboardingTextField(
            value = relationship,
            onValueChange = { relationship = it },
            placeholder = lang.t("onboarding.family.rel.placeholder"),
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Memory + AI
        OnboardingLabel(lang.t("onboarding.family.memory.label"))
        Spacer(modifier = Modifier.height(8.dp))
        OnboardingTextEditor(
            value = memory,
            onValueChange = { memory = it },
            placeholder = lang.t("onboarding.family.memory.placeholder"),
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

        // AI generate button
        Button(
            onClick = {
                if (memory.isNotBlank() && !isGenerating) {
                    isGenerating = true
                    aiError = null
                    scope.launch {
                        try {
                            val expanded = GeminiService.expandMemory(
                                hint = memory,
                                userName = profile.name,
                                language = lang.language
                            )
                            memory = expanded
                        } catch (_: Throwable) {
                            aiError = lang.t("onboarding.ai.error")
                        } finally {
                            isGenerating = false
                        }
                    }
                }
            },
            enabled = memory.isNotBlank() && !isGenerating,
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentYellow,
                contentColor = Color.Black,
                disabledContainerColor = SurfaceVariant,
                disabledContentColor = OnSurface.copy(alpha = 0.3f),
            ),
            shape = RoundedCornerShape(24.dp),
        ) {
            if (isGenerating) {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = lang.t("onboarding.family.ai.generating"), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            } else {
                Text(text = lang.t("onboarding.family.ai.button"), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        if (aiError != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = aiError!!, color = Color(0xFFFFA000), fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        OnboardingPrimaryButton(
            text = lang.t("onboarding.family.continue"),
            enabled = canSave,
            onClick = {
                val member = FamilyMember(
                    name = name.trim(),
                    relationship = relationship.trim(),
                    memory1 = memory
                )
                appVM.addFamilyMember(member)
                onContinue()
            }
        )
        OnboardingSkipLink(text = lang.t("onboarding.family.skip"), onClick = onContinue)
        Spacer(modifier = Modifier.height(48.dp))
    }
}
