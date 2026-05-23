package ai.juntunen.welcomeback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import ai.juntunen.welcomeback.ui.MainAppScreen
import ai.juntunen.welcomeback.ui.onboarding.OnboardingContainerScreen
import ai.juntunen.welcomeback.ui.theme.WelcomeBackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            WelcomeBackTheme {
                val appVM: AppViewModel = viewModel()
                val profile by appVM.userProfile.collectAsStateWithLifecycle()

                if (profile.isOnboardingComplete) {
                    MainAppScreen(onReset = { appVM.resetToNewUser() })
                } else {
                    OnboardingContainerScreen()
                }
            }
        }
    }
}
