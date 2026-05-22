package ai.juntunen.welcomeback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ai.juntunen.welcomeback.ui.onboarding.OnboardingContainerScreen
import ai.juntunen.welcomeback.ui.theme.WelcomeBackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Splash → main draw transition
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            WelcomeBackTheme {
                // For v0.1, we drop straight into the onboarding container.
                // Later this will check userProfile.isOnboardingComplete and
                // route to ContentScreen (tabs) instead.
                OnboardingContainerScreen()
            }
        }
    }
}
