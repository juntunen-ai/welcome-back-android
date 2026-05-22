package ai.juntunen.welcomeback.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ai.juntunen.welcomeback.ui.theme.BackgroundDark

/**
 * 13-step onboarding flow — mirrors iOS `OnboardingContainerView.swift`.
 *
 * v0.1 only includes the first two screens (welcome + language) as a working
 * end-to-end smoke test. The remaining 11 steps will be ported in the next
 * milestone.
 */
enum class OnboardingStep {
    WELCOME, LANGUAGE,
    MODEL_DOWNLOAD,
    PROFILE, PHOTO,
    ADD_FAMILY, FAMILY_TIP,
    ADD_PLACE, PLACE_TIP,
    ADD_STORY, STORY_TIP,
    MUSIC, COMPLETE
}

@Composable
fun OnboardingContainerScreen() {
    var step: OnboardingStep by rememberSaveable { mutableStateOf(OnboardingStep.WELCOME) }

    fun advance() {
        step = when (step) {
            OnboardingStep.WELCOME       -> OnboardingStep.LANGUAGE
            OnboardingStep.LANGUAGE      -> OnboardingStep.MODEL_DOWNLOAD
            OnboardingStep.MODEL_DOWNLOAD -> OnboardingStep.PROFILE
            OnboardingStep.PROFILE       -> OnboardingStep.PHOTO
            OnboardingStep.PHOTO         -> OnboardingStep.ADD_FAMILY
            OnboardingStep.ADD_FAMILY    -> OnboardingStep.FAMILY_TIP
            OnboardingStep.FAMILY_TIP    -> OnboardingStep.ADD_PLACE
            OnboardingStep.ADD_PLACE     -> OnboardingStep.PLACE_TIP
            OnboardingStep.PLACE_TIP     -> OnboardingStep.ADD_STORY
            OnboardingStep.ADD_STORY     -> OnboardingStep.STORY_TIP
            OnboardingStep.STORY_TIP     -> OnboardingStep.MUSIC
            OnboardingStep.MUSIC         -> OnboardingStep.COMPLETE
            OnboardingStep.COMPLETE      -> OnboardingStep.COMPLETE
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        AnimatedContent(
            targetState = step,
            transitionSpec = {
                (slideInHorizontally(animationSpec = tween(380)) { it } + fadeIn())
                    .togetherWith(slideOutHorizontally(animationSpec = tween(380)) { -it } + fadeOut())
            },
            label = "onboarding-step"
        ) { current ->
            when (current) {
                OnboardingStep.WELCOME  -> OnboardingWelcomeScreen(onContinue = ::advance)
                OnboardingStep.LANGUAGE -> OnboardingLanguageScreen(onContinue = ::advance)
                else -> ComingSoonScreen(stepName = current.name, onContinue = ::advance)
            }
        }
    }
}
