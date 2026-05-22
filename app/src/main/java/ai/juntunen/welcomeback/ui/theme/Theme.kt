package ai.juntunen.welcomeback.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Welcome Back is always dark — onboarding, conversations, memories all read
// best on the warm-yellow-on-black palette. We expose only a dark scheme.
private val WelcomeBackDarkScheme = darkColorScheme(
    primary           = AccentYellow,
    onPrimary         = Color.Black,
    background        = BackgroundDark,
    onBackground      = OnSurface,
    surface           = Surface,
    onSurface         = OnSurface,
    surfaceVariant    = SurfaceVariant,
    onSurfaceVariant  = OnSurface.copy(alpha = 0.7f),
    secondary         = AccentYellow,
    onSecondary       = Color.Black
)

@Composable
fun WelcomeBackTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? android.app.Activity)?.window
            if (window != null) {
                window.statusBarColor = BackgroundDark.toArgb()
                window.navigationBarColor = BackgroundDark.toArgb()
                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = false
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = WelcomeBackDarkScheme,
        typography  = WelcomeBackTypography,
        content     = content
    )
}
