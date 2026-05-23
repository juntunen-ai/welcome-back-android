package ai.juntunen.welcomeback.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ai.juntunen.welcomeback.ui.theme.AccentYellow
import ai.juntunen.welcomeback.ui.theme.BackgroundDark
import ai.juntunen.welcomeback.ui.theme.OnSurface

/** Logical order of the 13 onboarding steps. */
enum class OnboardingStep {
    WELCOME, LANGUAGE,
    MODEL_DOWNLOAD,
    PROFILE, PHOTO,
    ADD_FAMILY, FAMILY_TIP,
    ADD_PLACE, PLACE_TIP,
    ADD_STORY, STORY_TIP,
    MUSIC, COMPLETE
}

/** 13-step onboarding container. Mirrors iOS `OnboardingContainerView`. */
@Composable
fun OnboardingContainerScreen(onFinish: () -> Unit = {}) {
    var step: OnboardingStep by rememberSaveable { mutableStateOf(OnboardingStep.WELCOME) }

    fun advance() {
        step = when (step) {
            OnboardingStep.WELCOME        -> OnboardingStep.LANGUAGE
            OnboardingStep.LANGUAGE       -> OnboardingStep.MODEL_DOWNLOAD
            OnboardingStep.MODEL_DOWNLOAD -> OnboardingStep.PROFILE
            OnboardingStep.PROFILE        -> OnboardingStep.PHOTO
            OnboardingStep.PHOTO          -> OnboardingStep.ADD_FAMILY
            OnboardingStep.ADD_FAMILY     -> OnboardingStep.FAMILY_TIP
            OnboardingStep.FAMILY_TIP     -> OnboardingStep.ADD_PLACE
            OnboardingStep.ADD_PLACE      -> OnboardingStep.PLACE_TIP
            OnboardingStep.PLACE_TIP      -> OnboardingStep.ADD_STORY
            OnboardingStep.ADD_STORY      -> OnboardingStep.STORY_TIP
            OnboardingStep.STORY_TIP      -> OnboardingStep.MUSIC
            OnboardingStep.MUSIC          -> OnboardingStep.COMPLETE
            OnboardingStep.COMPLETE       -> OnboardingStep.COMPLETE
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        // Progress dots — show on the 7 milestone steps (mirrors iOS)
        val milestones = listOf(
            OnboardingStep.PROFILE, OnboardingStep.PHOTO,
            OnboardingStep.ADD_FAMILY, OnboardingStep.ADD_PLACE,
            OnboardingStep.ADD_STORY, OnboardingStep.MUSIC,
            OnboardingStep.COMPLETE
        )
        val milestoneIndex = milestones.indexOf(step)
        if (milestoneIndex >= 0) {
            ProgressDots(
                currentIndex = milestoneIndex,
                total = milestones.size,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 56.dp)
            )
        }

        AnimatedContent(
            targetState = step,
            transitionSpec = {
                (slideInHorizontally(animationSpec = tween(380)) { it } + fadeIn())
                    .togetherWith(slideOutHorizontally(animationSpec = tween(380)) { -it } + fadeOut())
            },
            label = "onboarding-step"
        ) { current ->
            when (current) {
                OnboardingStep.WELCOME        -> OnboardingWelcomeScreen(onContinue = ::advance)
                OnboardingStep.LANGUAGE       -> OnboardingLanguageScreen(onContinue = ::advance)
                OnboardingStep.MODEL_DOWNLOAD -> OnboardingModelDownloadScreen(onContinue = ::advance)
                OnboardingStep.PROFILE        -> OnboardingProfileScreen(onContinue = ::advance)
                OnboardingStep.PHOTO          -> OnboardingPhotoScreen(onContinue = ::advance)
                OnboardingStep.ADD_FAMILY     -> OnboardingAddFamilyScreen(onContinue = ::advance)
                OnboardingStep.FAMILY_TIP     -> OnboardingFamilyTipScreen(onContinue = ::advance)
                OnboardingStep.ADD_PLACE      -> OnboardingAddPlaceScreen(onContinue = ::advance)
                OnboardingStep.PLACE_TIP      -> OnboardingPlaceTipScreen(onContinue = ::advance)
                OnboardingStep.ADD_STORY      -> OnboardingAddStoryScreen(onContinue = ::advance)
                OnboardingStep.STORY_TIP      -> OnboardingStoryTipScreen(onContinue = ::advance)
                OnboardingStep.MUSIC          -> OnboardingMusicScreen(onContinue = ::advance)
                OnboardingStep.COMPLETE       -> OnboardingCompleteScreen(onDone = onFinish)
            }
        }
    }
}

@Composable
private fun ProgressDots(currentIndex: Int, total: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until total) {
            Box(
                modifier = Modifier
                    .width(if (i == currentIndex) 24.dp else 8.dp)
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(if (i <= currentIndex) AccentYellow else OnSurface.copy(alpha = 0.18f))
            )
        }
    }
}
