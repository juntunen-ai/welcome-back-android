# Changelog

All notable changes to the Welcome Back Android app are documented in this file.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and the
project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [v0.2.0] – Onboarding port – 2026-05-23

### Added
- All 11 remaining onboarding screens ported from iOS v1.3 — full 13-step
  guided flow now works end-to-end:
  - Model download (stub for v0.3 — explanation + "Download later" advance)
  - Profile (name with updated privacy subtitle)
  - Photo (dedicated photo picker step via PickVisualMedia)
  - Add Family Member (name + relationship + memory + AI-expand button)
  - Family Tip (link to Settings → Family Members)
  - Add Place (name + description + AI-write button)
  - Place Tip (link to Settings → Places)
  - Add Story (title + story text + AI-write button)
  - Story Tip (link to Settings → Memories & Stories)
  - Music (Apple Music → Spotify explainer; SDK wired in v0.5)
  - Complete (animated checkmark + tips card + "Start Remembering" CTA)
- `AppViewModel` (Activity-scoped) exposes `UserProfile` as `StateFlow` with
  update methods (`updateName`, `addFamilyMember`, `addPlace`, `addMemory`,
  `completeOnboarding`).
- `UserProfileRepository` persists the profile as serialised JSON in
  Preferences DataStore, so reopening the app remembers whether onboarding
  was completed and all saved content.
- `GeminiService` — Ktor REST client for `gemini-2.0-flash`. Implements
  `expandMemory(hint, userName, language)` mirroring iOS. Returns the hint
  unchanged when no API key is configured.
- `PhotoStorage` utility — copies picked images into the app's internal
  files dir and returns a stable `photo:…` URL string.
- Reusable Compose widgets (`OnboardingLabel`, `OnboardingTextField`,
  `OnboardingPrimaryButton`, `OnboardingSkipLink`) to keep all 13 screens
  visually consistent.
- Progress dot indicator shown on the 7 milestone steps (profile through
  complete), mirroring iOS.

### Changed
- `OnboardingContainerScreen` now wires every step and replaces the
  `ComingSoonScreen` placeholder with the real screens.
- `MainActivity` checks `userProfile.isOnboardingComplete` and routes to
  `MainAppPlaceholderScreen` after onboarding, or restarts onboarding for
  new users. Full tab navigation arrives in v0.4.
- `Strings.kt` expanded from ~25 keys to ~140 keys covering every onboarding
  screen, in both English and Finnish.

### Notes
- Spotify SDK is still deferred to v0.5; the Music screen shows the
  explainer but the Connect button just advances for now.
- On-device Gemma download is deferred to v0.3; the Model screen explains
  why and lets users continue.

## [v0.1.0] – Scaffolding – 2026-04-26

### Added
- Initial Android project skeleton — Kotlin DSL, Compose BOM 2024.09.03,
  Material 3, Navigation Compose, Ktor, Coil, Room, MediaPipe GenAI dep
  declared, Maps Compose.
- Dark Material 3 theme with the iOS palette (BackgroundDark #0A0A0A,
  AccentYellow #FFD600).
- Splash screen + adaptive launcher icon (placeholder yellow heart).
- `LanguageManager` (runtime English / Finnish switcher) and a starter
  `Strings.kt` covering welcome + language picker screens.
- Domain models ported from iOS: `FamilyMember`, `Memory`, `Place`,
  `UserProfile`, `MemoryCategory`, `VoiceMode`.
- First two onboarding screens working end-to-end:
  - Welcome (pulsing yellow heart + Get Started CTA)
  - Language picker (EN / FI cards, recomposes immediately on selection)
- `local.properties.example` template for API keys.
- `data_extraction_rules.xml` opts out of automatic Android backup and
  device-transfer to preserve the "data stays on your phone" promise.
- Comprehensive README documenting architecture, deviations from iOS, and
  the roadmap to v1.0.

[Unreleased]: https://github.com/juntunen-ai/welcome-back-android/compare/v0.2.0...HEAD
[v0.2.0]: https://github.com/juntunen-ai/welcome-back-android/compare/v0.1.0...v0.2.0
[v0.1.0]: https://github.com/juntunen-ai/welcome-back-android/releases/tag/v0.1.0
