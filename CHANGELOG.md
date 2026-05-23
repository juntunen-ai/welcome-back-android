# Changelog

All notable changes to the Welcome Back Android app are documented in this file.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and the
project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [v0.3.0] – Main app – iOS feature parity – 2026-05-23

### Added
- **HomeScreen** — greeting, GPS location card (taps open Google Maps), large
  pulsing mic button, identity card with photo + bio, family horizontal scroll
  row, and About card.
- **FamilyScreen** — album-card list view with 200 dp hero images, gradient
  scrim, name/relationship overlay.
- **FamilyMemberProfileScreen** — horizontal photo pager, dot indicators,
  Biography/Memory section cards with star icons, floating "Hear [Name]'s
  Story" TTS button with pulse animation.
- **FamilyMemberDetailScreen** — full add/edit form (name, relationship,
  phone, biography, memory1, memory2), `PickVisualMedia` photo picker,
  delete with confirmation dialog.
- **MemoriesScreen** — combined tab showing family list, places list, and
  memory story cards grouped by category with colour badges.
- **MemoryStoryDetailScreen** — full-screen story view with hero image/gradient,
  category badge, date, description, and floating TTS hear button.
- **PlaceDetailScreen** — place hero image (or purple gradient fallback),
  description card, "Open in Maps" button when coordinates are present.
- **MusicScreen** — placeholder with Spotify integration teaser (v0.5).
- **VoiceSessionViewModel** — orchestrates STT → Gemini → TTS pipeline;
  exposes `Phase` state (IDLE/LISTENING/THINKING/SPEAKING/ERROR).
- **ListeningScreen** — full-screen overlay: live transcript, AI reply card,
  pulsing mic ring, stop/start button, dismiss X.
- **SettingsScreen** — profile card, Content / Preferences / Legal sections,
  reset with confirmation.
- **PersonalInfoScreen** — edit name, address, biography, profile photo.
- **FamilyManagementScreen** — list + FAB to add; taps navigate to edit form.
- **MemoriesManagementScreen** — memory list with category colour dots + FAB.
- **MemoryDetailEditScreen** — add/edit memory (photo, title, date, category
  selector, description, delete).
- **PlacesManagementScreen** — place list + FAB.
- **PlaceDetailEditScreen** — add/edit place (photo, name, description, delete).
- **NotificationsSettingsScreen** — enable toggle, time checkboxes (morning /
  noon / afternoon / evening), custom topics text field.
- **LanguageSettingsScreen** — EN / FI picker; applies immediately.
- **VoiceModeSettingsScreen** — Cloud vs On-Device radio with v0.3 note.
- **ModelSettingsScreen** — shows active model (Gemini 2.0 Flash), on-device
  "coming in v0.3" stub card.
- **LegalScreen** — Privacy Policy and Terms of Use text viewer.
- **TtsService** — `TextToSpeech` wrapper with `isPlaying: StateFlow<Boolean>`,
  `suspend fun speak()`, `stop()`, `release()`.
- **SttService** — `SpeechRecognizer` wrapper with partial results,
  `transcriptFlow`, `finalResultFlow`, `errorFlow`.
- **LocationService** — `FusedLocationProviderClient` one-shot + `Geocoder`
  reverse geocoding; `city`, `streetAddress`, `coordinate` StateFlows.
- **NotificationService** — WorkManager daily reminders via `ReminderWorker`
  with 8-message pool; `scheduleReminders()`, `cancelAll()`, `createChannel()`.
- **GeminiService.chat()** — conversational AI reply using UserProfile as
  system context, bilingual EN/FI prompts.
- **MemberImage** shared composable — loads from `PhotoStorage`, falls back to
  deterministic gradient avatar (initials, 6 colour palettes by name hash).
- **MainNavGraph / MainAppScreen** — 5-tab `NavigationBar` (Home, Memories,
  Family, Music, Settings) with `NavHost`, full-screen `ListeningScreen`
  overlay, tab sync via `AppViewModel.selectedTab`.
- `gradlew` + `gradle-wrapper.jar` added so the project builds without needing
  a local Gradle installation.
- **Strings.kt** expanded to ~280 keys covering all new screens in EN and FI.

### Changed
- `AppViewModel` rewritten with `updateAndGet` pattern to eliminate race
  condition when saving profile snapshots; navigation state flows added.
- `MainActivity` updated to use `MainAppScreen` (real navigation) instead of
  the v0.2 placeholder; uses `collectAsStateWithLifecycle`.
- `Models.kt` adds `NotificationTime` enum and `AppTab` enum.
- `build.gradle.kts` — new dependencies (location, WorkManager, Accompanist
  permissions); properties reading moved to top level to fix Kotlin DSL
  resolution error.
- `OnboardingWelcomeScreen.kt` — fixed `animateFloat` unresolved reference
  (changed to wildcard animation.core import).

### Notes
- On-device TTS/STT use Android built-ins; voice cloning and local LLM are
  deferred to v0.4.
- Spotify integration deferred to v0.5.

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

[Unreleased]: https://github.com/juntunen-ai/welcome-back-android/compare/v0.3.0...HEAD
[v0.3.0]: https://github.com/juntunen-ai/welcome-back-android/compare/v0.2.0...v0.3.0
[v0.2.0]: https://github.com/juntunen-ai/welcome-back-android/compare/v0.1.0...v0.2.0
[v0.1.0]: https://github.com/juntunen-ai/welcome-back-android/releases/tag/v0.1.0
