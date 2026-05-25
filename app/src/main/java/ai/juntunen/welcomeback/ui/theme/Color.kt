package ai.juntunen.welcomeback.ui.theme

import androidx.compose.ui.graphics.Color

// ── Brand ─────────────────────────────────────────────────────────────────────
val AccentYellow = Color(0xFFFFD600)

// ── Backgrounds ───────────────────────────────────────────────────────────────
val BackgroundDark = Color(0xFF0A0A0A)

// ── Surface container family (M3 tonal elevation tiers) ──────────────────────
// Use these instead of SurfaceVariant.copy(alpha=x).
// Lower number = less prominent / lower elevation.
val SurfaceContainerLowest  = Color(0xFF0F0F0F)   // barely lifted
val SurfaceContainerLow     = Color(0xFF161616)   // subtle cards
val SurfaceContainer        = Color(0xFF1C1C1C)   // standard card
val SurfaceContainerHigh    = Color(0xFF252525)   // elevated card / modal
val SurfaceContainerHighest = Color(0xFF2E2E2E)   // top-most overlay

// Legacy aliases kept so existing code compiles unchanged.
val Surface        = SurfaceContainerLow
val SurfaceVariant = SurfaceContainerHigh

// ── Content ───────────────────────────────────────────────────────────────────
val OnSurface = Color(0xFFFFFFFF)

// ── Outline / dividers ────────────────────────────────────────────────────────
val Outline        = Color(0xFF3A3A3A)
val OutlineVariant = Color(0xFF242424)

// ── Primary container (chips, selected states) ────────────────────────────────
val PrimaryContainer   = Color(0xFF433200)
val OnPrimaryContainer = Color(0xFFFFE27A)

// ── Inverse (toasts, snackbars) ───────────────────────────────────────────────
val InverseSurface   = Color(0xFFE8E6E0)
val InverseOnSurface = Color(0xFF1C1B16)
val InversePrimary   = Color(0xFF7A5900)
