package ai.juntunen.welcomeback.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Welcome Back is always dark — onboarding, conversations, and memories all
 * read best on the warm-yellow-on-black palette.
 *
 * The color scheme maps every M3 role so standard components (Button,
 * NavigationBar, Card, TopAppBar, BottomSheet…) work correctly without
 * needing custom overrides.
 *
 * Surface container hierarchy (low → high elevation):
 *   surfaceContainerLowest  — nav bar / sheet backdrop
 *   surfaceContainerLow     — subtle chip / input background
 *   surfaceContainer        — standard card
 *   surfaceContainerHigh    — elevated card / modal sheet
 *   surfaceContainerHighest — top-most overlay (dialogs)
 */
private val WelcomeBackDarkScheme = darkColorScheme(
    // ── Primary ──────────────────────────────────────────────────────────────
    primary            = AccentYellow,
    onPrimary          = Color.Black,
    primaryContainer   = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,

    // ── Secondary (muted amber — filter chips, selected state) ───────────────
    secondary              = Color(0xFFD4C38A),
    onSecondary            = Color(0xFF383016),
    secondaryContainer     = Color(0xFF504622),
    onSecondaryContainer   = Color(0xFFF1DEA6),

    // ── Tertiary (soft teal — nature / places category accent) ───────────────
    tertiary               = Color(0xFFA8CDB8),
    onTertiary             = Color(0xFF113525),
    tertiaryContainer      = Color(0xFF284C3A),
    onTertiaryContainer    = Color(0xFFC3E9D3),

    // ── Error ─────────────────────────────────────────────────────────────────
    error                  = Color(0xFFFFB4AB),
    onError                = Color(0xFF690005),
    errorContainer         = Color(0xFF93000A),
    onErrorContainer       = Color(0xFFFFDAD6),

    // ── Background / base ─────────────────────────────────────────────────────
    background             = BackgroundDark,
    onBackground           = OnSurface,

    // ── Surface (lowest layer — used by Scaffold, ModalBottomSheet) ───────────
    surface                = SurfaceContainerLowest,
    onSurface              = OnSurface,
    surfaceVariant         = SurfaceContainerHigh,
    onSurfaceVariant       = Color(0xFFCAC4B4),

    // ── Surface container family (tonal elevation tiers) ──────────────────────
    surfaceContainerLowest  = SurfaceContainerLowest,
    surfaceContainerLow     = SurfaceContainerLow,
    surfaceContainer        = SurfaceContainer,
    surfaceContainerHigh    = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,

    // ── Outline / dividers ────────────────────────────────────────────────────
    outline                = Outline,
    outlineVariant         = OutlineVariant,

    // ── Scrim (modal overlays) ────────────────────────────────────────────────
    scrim                  = Color(0xFF000000),

    // ── Inverse (Snackbar, Tooltip) ───────────────────────────────────────────
    inverseSurface         = InverseSurface,
    inverseOnSurface       = InverseOnSurface,
    inversePrimary         = InversePrimary,

    // ── Tint (tonal elevation on Cards, FABs etc.) ────────────────────────────
    surfaceTint            = AccentYellow.copy(alpha = 0.08f),
)

@Composable
fun WelcomeBackTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Edge-to-edge is enabled in MainActivity via enableEdgeToEdge().
    // window.statusBarColor is deprecated on API 35+ — do NOT set it.
    // We only need to ensure system bar icons remain light (white on dark).
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars     = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = WelcomeBackDarkScheme,
        typography  = WelcomeBackTypography,
        shapes      = WelcomeBackShapes,
        content     = content
    )
}
