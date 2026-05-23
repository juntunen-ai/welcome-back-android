# Welcome Back Android — Developer Guide

This guide is written for a developer who is picking up the project for the first time, or continuing work after a break. It covers the app's purpose, every source file, and step-by-step implementation instructions for each remaining roadmap phase.

---

## Table of contents

1. [App purpose and user context](#1-app-purpose-and-user-context)
2. [Repository overview](#2-repository-overview)
3. [Getting started](#3-getting-started)
4. [Architecture](#4-architecture)
5. [File reference — every source file explained](#5-file-reference)
6. [Localisation system](#6-localisation-system)
7. [Roadmap — implementation guide for each phase](#7-roadmap)
   - [v0.3 — On-device AI (Gemma 4)](#v03--on-device-ai-gemma-4)
   - [v0.4 — Main app UI](#v04--main-app-ui)
   - [v0.5 — Voice and Music](#v05--voice-and-music)
   - [v0.6 — Maps, notifications, polish](#v06--maps-notifications-polish)
   - [v1.0 — Google Play release](#v10--google-play-release)
8. [Patterns and conventions](#8-patterns-and-conventions)
9. [Known issues and constraints](#9-known-issues-and-constraints)
10. [iOS parity reference](#10-ios-parity-reference)

---

## 1. App purpose and user context

**Welcome Back** is a compassionate AI companion designed for people living with memory challenges — dementia, Alzheimer's disease, or age-related memory loss — and their close family members.

### The problem it solves

Memory-challenged individuals often struggle with:
- Remembering who their family members are and what they look like
- Recalling important places and life stories
- Feeling isolated and anxious when they cannot place familiar faces

Family caregivers struggle with:
- Being available around the clock to answer the same questions
- Finding ways to communicate warmth and familiarity at a distance

### What the app does

Welcome Back acts as a gentle AI companion that:

- **Recognises the user's world** — their name, photo, family members with faces and relationships, favourite places, music, and life stories are all stored locally on the device
- **Has a voice conversation** — the user can speak to the app; it answers questions like "Who is this person in the photo?" or "Where do I live?" with warm, personal answers
- **Plays familiar music** — Spotify integration to stream the songs they know and love
- **Shows family faces** — a family tab with photos and short personalised bios that can be read aloud
- **Stores memories and stories** — a curated collection of life events the user can browse and hear

### Design principles

1. **Calm and unhurried** — large text, simple layout, no cognitive overload
2. **Privacy first** — all personal data stays on the device; no accounts required; no cloud sync of personal content
3. **Warm tone** — every AI response is empathetic, first-person, personal
4. **Family-assisted setup** — onboarding is designed so a family member can help fill in the details

### Users

- **Primary user:** the memory-challenged person (the "user" throughout the code)
- **Secondary user:** a family member or caregiver who sets up and maintains the content

---

## 2. Repository overview

```
welcome-back-android/
├── app/
│   ├── build.gradle.kts              # App-level build config + dependencies
│   ├── proguard-rules.pro            # Keep rules for release minification
│   └── src/main/
│       ├── AndroidManifest.xml       # Permissions + activity declaration
│       ├── java/ai/juntunen/welcomeback/
│       │   ├── MainActivity.kt
│       │   ├── WelcomeBackApp.kt
│       │   ├── AppViewModel.kt
│       │   ├── LanguageManager.kt
│       │   ├── Strings.kt
│       │   ├── data/
│       │   │   ├── Models.kt
│       │   │   └── UserProfileRepository.kt
│       │   ├── services/
│       │   │   ├── GeminiService.kt
│       │   │   └── PhotoStorage.kt
│       │   └── ui/
│       │       ├── theme/
│       │       │   ├── Color.kt
│       │       │   ├── Type.kt
│       │       │   └── Theme.kt
│       │       ├── onboarding/
│       │       │   ├── OnboardingContainerScreen.kt
│       │       │   ├── OnboardingComponents.kt
│       │       │   ├── OnboardingWelcomeScreen.kt
│       │       │   ├── OnboardingLanguageScreen.kt
│       │       │   ├── OnboardingModelDownloadScreen.kt
│       │       │   ├── OnboardingProfileScreen.kt
│       │       │   ├── OnboardingPhotoScreen.kt
│       │       │   ├── OnboardingAddFamilyScreen.kt
│       │       │   ├── OnboardingTipScreens.kt
│       │       │   ├── OnboardingAddPlaceScreen.kt
│       │       │   ├── OnboardingAddStoryScreen.kt
│       │       │   ├── OnboardingMusicScreen.kt
│       │       │   └── OnboardingCompleteScreen.kt
│       │       └── home/
│       │           └── MainAppPlaceholderScreen.kt
│       └── res/
│           ├── drawable/splash_icon.xml
│           ├── mipmap-anydpi-v26/    # Adaptive launcher icon
│           ├── values/               # colors.xml, strings.xml, themes.xml
│           └── xml/data_extraction_rules.xml
├── build.gradle.kts                  # Project-level build config
├── settings.gradle.kts               # Module list + plugin management
├── gradle/libs.versions.toml         # Version catalog (if present)
├── local.properties                  # Your secrets — NOT in git
├── local.properties.example          # Template for new developers
├── CHANGELOG.md                      # Version history
└── README.md                         # Quick start
```

**Companion iOS repository:** https://github.com/juntunen-ai/welcome-back  
The Android app is a port of the iOS v1.3 codebase. When the iOS app adds a feature, mirror it here.

---

## 3. Getting started

### Prerequisites

| Tool | Minimum version | Where to get it |
|------|----------------|-----------------|
| Android Studio | Ladybug 2024.2.1 | https://developer.android.com/studio |
| JDK | 17 | Bundled with Android Studio |
| Android SDK | API 35 | Android Studio SDK Manager |
| Device / emulator | Android 8.0 (API 26) | AVD Manager |

### Clone and open

```bash
git clone https://github.com/juntunen-ai/welcome-back-android.git
cd welcome-back-android
```

Open the project root in Android Studio. It will detect the Gradle wrapper and offer to download Gradle 8.9 (~150 MB). Accept and wait.

### Configure API keys

```bash
cp local.properties.example local.properties
```

Edit `local.properties` and fill in the values you have:

```properties
# Android Studio sets this automatically on first open.
sdk.dir=/Users/YOU/Library/Android/sdk

# Google AI Studio — https://aistudio.google.com/app/apikey
# Required for AI memory expansion during onboarding.
GEMINI_API_KEY=AIzaSy...

# Google Cloud Console — Text-to-Speech API (needed from v0.5)
# https://console.cloud.google.com/apis/library/texttospeech.googleapis.com
GOOGLE_TTS_API_KEY=

# Google Cloud Console — Maps SDK for Android (needed from v0.6)
MAPS_API_KEY=

# Spotify developer dashboard — https://developer.spotify.com/dashboard (v0.5)
SPOTIFY_CLIENT_ID=
```

Keys are read at build time and compiled into `BuildConfig` as string constants. They are never transmitted to any server other than the respective API endpoint, and they are in `.gitignore`.

### Run the app

Press **Run ▶︎** in Android Studio.

The first build downloads all Gradle dependencies (~500 MB). Subsequent builds are fast (incremental).

```bash
# Command-line alternative (after first Android Studio build sets up the wrapper):
./gradlew :app:installDebug
```

### Run on a real device

1. Enable Developer Options on the device (tap Build Number 7 times in Settings → About Phone)
2. Enable USB Debugging
3. Connect via USB
4. Select the device in Android Studio's device picker

---

## 4. Architecture

### Pattern: MVVM + single-activity

```
Activity (MainActivity)
    └── Compose content tree
            ├── AppViewModel (AndroidViewModel, Activity-scoped)
            │       └── UserProfile : StateFlow<UserProfile>
            ├── OnboardingContainerScreen   ← if !isOnboardingComplete
            └── MainAppPlaceholderScreen    ← if isOnboardingComplete
```

- **One `Activity`** — `MainActivity`. The entire UI is Compose.
- **One shared ViewModel** — `AppViewModel`. Every screen obtains it via `viewModel()` and reads from / writes to `userProfile`.
- **No Navigation Compose graph yet** — onboarding steps are managed by an `enum` state variable inside `OnboardingContainerScreen`. When the main app tabs arrive (v0.4), add a proper `NavHost`.
- **No Room database yet** — the single `UserProfile` object is stored as a JSON blob in DataStore Preferences. Room is declared as a dependency but not yet used. Use it for large lists in v0.4 (memories, conversations).

### State flow

```
User action (button tap / text input)
    ↓
Composable calls appVM.updateXxx() / appVM.addXxx()
    ↓
AppViewModel.update { ... }   // transforms the in-memory StateFlow
    ↓
UserProfileRepository.save()  // persists to DataStore on IO thread
    ↓
StateFlow emits new value
    ↓
All subscribed Composables recompose
```

### Language switching

```
LanguageManager.setLanguage(AppLanguage.FINNISH)
    ↓
LanguageManager._language Compose state changes
    ↓
Any Composable that calls lang.t("key") recomposes immediately
    ↓
No Activity restart required
```

---

## 5. File reference

### `MainActivity.kt`

Entry point for the entire app. Reads `userProfile.isOnboardingComplete` from `AppViewModel` and routes to either `OnboardingContainerScreen` or `MainAppPlaceholderScreen`.

**When to edit:** when adding new top-level navigation destinations (v0.4 — replace `MainAppPlaceholderScreen` with the real tab container).

```kotlin
// Routing logic — straightforward:
if (profile.isOnboardingComplete) {
    MainAppPlaceholderScreen(onReset = { ... })
} else {
    OnboardingContainerScreen()
}
```

---

### `WelcomeBackApp.kt`

Custom `Application` subclass. Currently initialises `LanguageManager` with the application context so it can read from SharedPreferences.

**When to edit:** add any other singleton that needs an application context at startup (e.g., a Room database, a Coil image loader config, Firebase initialisation).

---

### `AppViewModel.kt`

The central state holder. `AndroidViewModel` subclass so it survives configuration changes (screen rotation) and lives as long as the Activity.

| Method | What it does |
|--------|-------------|
| `updateName(name)` | Updates `UserProfile.name` |
| `updateProfileImageURL(url)` | Updates the profile photo token |
| `addFamilyMember(member)` | Appends to `familyMembers` list |
| `addPlace(place)` | Appends to `places` list |
| `addMemory(memory)` | Appends to `memories` list |
| `completeOnboarding()` | Sets `isOnboardingComplete = true` → MainActivity switches to main app |
| `resetToNewUser()` | Clears DataStore and resets StateFlow to `UserProfile.DEFAULT` |

**Pattern:** every public method calls the private `update { transform }` helper, which (1) applies the transform to the in-memory `StateFlow` and (2) persists to DataStore on the viewModelScope IO dispatcher.

**When to edit (v0.4):** add `updateBiography()`, `updateAddress()`, `removeFamilyMember()`, `updateFamilyMember()`, etc. for the Settings screens.

---

### `LanguageManager.kt`

Singleton (`object`) that holds the selected `AppLanguage` as Compose observable state. Every `@Composable` that calls `lang.t("some.key")` automatically recomposes when the language changes.

**`AppLanguage` enum:**

| Value | `code` | `displayName` | `flag` |
|-------|--------|--------------|--------|
| `ENGLISH` | `"en"` | `"English"` | 🇬🇧 |
| `FINNISH` | `"fi"` | `"Suomi"` | 🇫🇮 |

**Adding a new language** (e.g., Swedish):
1. Add `SWEDISH("sv", "Svenska", "🇸🇪")` to the enum
2. Add a `sv` map to `Strings.kt`
3. Add a card to `OnboardingLanguageScreen`

**When to edit:** adding languages, or reading/writing the persisted language code.

---

### `Strings.kt`

Contains two `Map<String, String>` objects — `Strings.en` and `Strings.fi` — covering every user-visible string in the app. This avoids `strings.xml` so the language can be switched at runtime without restarting the Activity (which is how Android's built-in localisation works).

**Key naming convention:** `<screen>.<element>.<variant>`, e.g.:
- `"onboarding.welcome.title"` → screen: onboarding, element: welcome, variant: title
- `"onboarding.family.ai.button"` → AI expand button on the family screen

**Format strings:** use `%s` (Kotlin `String.format`) — **not** `%@` (Swift/iOS). Example:
```kotlin
"onboarding.complete.title" to "You're all set%s!"
// Called as: lang.t("onboarding.complete.title", ", Harri") → "You're all set, Harri!"
```

**When to edit:** whenever a new screen or string is added. Always add both the English and Finnish versions. If Finnish translation is not yet available, copy the English string — the fallback chain in `LanguageManager.t()` will use English anyway, but having an explicit entry prevents the raw key from showing up.

---

### `data/Models.kt`

Domain model classes. Direct port from `Models.swift` in the iOS repo. All classes are annotated with `@Serializable` so they can be round-tripped through `kotlinx.serialization`.

| Class | Purpose |
|-------|---------|
| `FamilyMember` | A person in the user's family — name, relationship, photo URL, memory texts, optional voice clone ID |
| `Memory` | A life story or event — title, date, photo URL, category, description text |
| `MemoryCategory` | `FAMILY`, `EVENTS`, `PLACES`, `OTHER` |
| `Place` | A significant location — name, description, photo URL, lat/lon |
| `VoiceMode` | `CLOUD` (Google TTS) or `LOCAL` (on-device, future) |
| `UserProfile` | The root object that wraps everything — name, photo, lists of family/memories/places, flags |

**Adding new fields:** add with a default value so existing DataStore JSON (missing the field) deserialises without error:
```kotlin
val newField: String = ""  // ✅ safe to add
val newField: String       // ❌ will crash on old data
```

---

### `data/UserProfileRepository.kt`

Reads and writes `UserProfile` to DataStore Preferences as a single JSON string.

**Why one JSON blob instead of individual preference keys?** The profile has nested lists (family members with their own photo URLs and memory texts). Flattening that into individual keys is brittle. One JSON blob serialised by `kotlinx.serialization` is simpler, handles nesting naturally, and is migration-friendly — adding new fields with defaults requires no migration code.

**Exposed API:**

| Member | Type | Purpose |
|--------|------|---------|
| `profileFlow` | `Flow<UserProfile>` | Cold flow, emits on every DataStore write |
| `load()` | `suspend fun` | One-shot read — used at VM startup |
| `save(profile)` | `suspend fun` | Writes the full profile |
| `clear()` | `suspend fun` | Removes the profile key (dev reset) |

**DataStore setup:** the extension `val Context.profileDataStore` is a file-level private delegate. DataStore is a singleton per process; calling the delegate multiple times returns the same instance.

---

### `services/GeminiService.kt`

Calls `gemini-2.0-flash` to expand a short memory hint into a warm 2–3 sentence personal story.

**`expandMemory(hint, userName, language)`:**
- `hint` — the user's own brief description (e.g., "summer cottage in Lapland")
- `userName` — inserted into the prompt so the AI can personalise the output
- `language` — `AppLanguage.ENGLISH` or `AppLanguage.FINNISH`; drives a different system prompt
- Returns the expanded text, or `hint` unchanged if the API key is blank or the network call fails

**Safety:** if `BuildConfig.GEMINI_API_KEY` is empty (no key in `local.properties`), the function returns the hint immediately — the app works without an API key, just without AI expansion.

**Model config:** `temperature = 0.75`, `topP = 0.85`, `maxOutputTokens = 250`. These produce warm, fluent output without being overly verbose. Adjust if the outputs feel too short or too creative.

**When to extend:** v0.4 will need a `chat(messages, userName, language)` method for the main conversation screen. The HTTP client (`client`) is already set up — add a new method alongside `expandMemory`.

---

### `services/PhotoStorage.kt`

Copies a photo `Uri` (from the system photo picker) into the app's private internal storage and returns a stable `photo:<filename>` token string.

**Why this pattern?** Photo picker URIs are temporary — they become invalid after the session. Copying to internal storage gives a permanent path. Using a `photo:` prefixed token (instead of a raw file path) keeps the token stable even if the internal files directory moves between app versions.

**`savePhoto(context, uri, id)`** — copies the image and returns `"photo:profile.jpg"` (or whatever `id` is).  
**`fileFor(context, token)`** — resolves `"photo:profile.jpg"` back into a `File` for Coil to load.

**Using with Coil:**
```kotlin
val file = PhotoStorage.fileFor(context, profile.profileImageURL)
AsyncImage(model = file, contentDescription = null)
```

---

### `ui/theme/Color.kt`

The five colours that define the entire visual identity. These are a 1:1 match with the iOS asset catalog:

| Name | Hex | Usage |
|------|-----|-------|
| `BackgroundDark` | `#0A0A0A` | Screen backgrounds |
| `Surface` | `#161616` | Cards, bottom sheets |
| `SurfaceVariant` | `#1F1F1F` | Input fields, secondary cards |
| `OnSurface` | `#FFFFFF` | All text |
| `AccentYellow` | `#FFD600` | CTAs, icons, progress dots, highlights |

**Do not add new colours without discussing with the iOS developer.** The two apps must stay visually identical.

---

### `ui/theme/Type.kt` and `ui/theme/Theme.kt`

Standard Material 3 typography and theme setup. `Theme.kt` applies the dark colour scheme and sets the status bar to transparent (edge-to-edge). These rarely need editing.

---

### `ui/onboarding/OnboardingContainerScreen.kt`

The state machine that drives the entire 13-step onboarding flow. Contains:

1. **`OnboardingStep` enum** — `WELCOME`, `LANGUAGE`, `MODEL_DOWNLOAD`, `PROFILE`, `PHOTO`, `ADD_FAMILY`, `FAMILY_TIP`, `ADD_PLACE`, `PLACE_TIP`, `ADD_STORY`, `STORY_TIP`, `MUSIC`, `COMPLETE`

2. **`advance()` function** — maps every step to the next. The linear mapping makes the flow easy to read and easy to reorder.

3. **`AnimatedContent`** — slides new steps in from the right and old steps out to the left (380 ms tween + fade), matching the iOS transition.

4. **Progress dots** — shown only on the 7 "milestone" steps (PROFILE through COMPLETE). The active dot grows wider (24 dp) and all completed dots turn `AccentYellow`.

**To add a new step:** add a value to `OnboardingStep`, add a `when` branch in `advance()`, add a `when` branch in the `AnimatedContent` block, and create the new screen composable.

---

### `ui/onboarding/OnboardingComponents.kt`

Shared composables used by every onboarding screen:

| Composable | Purpose |
|-----------|---------|
| `OnboardingLabel` | Uppercase, small, dimmed section label above fields |
| `OnboardingTextField` | Single-line input — dark background, yellow cursor, no underline |
| `OnboardingTextEditor` | Multi-line version of the above — for memory/story text |
| `OnboardingPrimaryButton` | Full-width yellow pill button — the main CTA |
| `OnboardingSkipLink` | Small underlined text link — "Skip for now" |
| `OnboardingSettingsPill` | "⚙ Settings → X" pill shown on tip screens |

**When to edit:** if the visual language changes (border radius, font size, colours), update here and all 13 screens update at once.

---

### `ui/onboarding/OnboardingWelcomeScreen.kt`

First screen. Shows the app name and a pulsing yellow heart icon (infinite scale animation 0.92→1.0, 2.2 s cycle). Single CTA button to begin.

Strings used: `onboarding.welcome.title`, `.subtitle`, `.cta`, `.time`

---

### `ui/onboarding/OnboardingLanguageScreen.kt`

Two language cards (English / Finnish). Tapping a card calls `LanguageManager.setLanguage()` which immediately recomposes all visible text. The CTA becomes active after a language is selected.

---

### `ui/onboarding/OnboardingModelDownloadScreen.kt`

Explains that an on-device AI model (Gemma 4) will be downloaded. Currently a stub — the "Download" button just advances to the next step. **Actual download logic is implemented in v0.3.**

---

### `ui/onboarding/OnboardingProfileScreen.kt`

Collects the user's name. On "Continue" calls `appVM.updateName(name)`. Has a privacy subtitle explaining that data stays on the device.

---

### `ui/onboarding/OnboardingPhotoScreen.kt`

Lets the user pick a profile photo from the gallery using `PickVisualMedia`. On selection:
1. Calls `PhotoStorage.savePhoto(context, uri, "profile")` → returns `"photo:profile.jpg"`
2. Calls `appVM.updateProfileImageURL("photo:profile.jpg")`
3. Advances automatically

Has a "Skip for now" link. Photo is shown as a preview once selected.

---

### `ui/onboarding/OnboardingAddFamilyScreen.kt`

Collects one family member: name, relationship, and a memory text. The memory text field has an "✦ Generate with AI" button that calls `GeminiService.expandMemory()` and replaces the field content with the expanded version. Shows a loading spinner while generating.

On "Continue":
```kotlin
val member = FamilyMember(name = ..., relationship = ..., memory1 = memoryText)
appVM.addFamilyMember(member)
```

Has a "Skip for now" link that advances without saving.

**Note:** only one family member is added in onboarding. Additional members are added in the Settings → Family tab (v0.4).

---

### `ui/onboarding/OnboardingTipScreens.kt`

Contains three public composables sharing a private `TipScreen` layout:

| Screen | Icon | Accent | Purpose |
|--------|------|--------|---------|
| `OnboardingFamilyTipScreen` | 👨‍👩‍👧‍👦 | Green `#34C759` | "You can add more family members later" |
| `OnboardingPlaceTipScreen` | 🗺️ | Blue `#4D9AFF` | "You can add more places later" |
| `OnboardingStoryTipScreen` | 📚 | Purple `#AF6BE6` | "You can add more stories later" |

Each tip screen shows a `OnboardingSettingsPill` pointing to the relevant Settings section.

---

### `ui/onboarding/OnboardingAddPlaceScreen.kt`

Collects one place: name and description. Same AI expand pattern as `OnboardingAddFamilyScreen`.

On "Continue":
```kotlin
appVM.addPlace(Place(name = placeName.trim(), description = description))
```

---

### `ui/onboarding/OnboardingAddStoryScreen.kt`

Collects one memory / life story: title and body text. Same AI expand pattern.

On "Continue":
```kotlin
val memory = Memory(title = storyTitle.trim(), description = storyText.trim(), category = MemoryCategory.OTHER)
appVM.addMemory(memory)
```

---

### `ui/onboarding/OnboardingMusicScreen.kt`

Explains the Spotify integration that's coming in v0.5. The "Connect Spotify" button currently just advances (same as "Skip"). No Spotify SDK calls yet.

**v0.5 TODO:** replace the `onContinue` call on the Connect button with the Spotify auth flow.

---

### `ui/onboarding/OnboardingCompleteScreen.kt`

Final onboarding screen. Shows:
- An animated checkmark (spring scale-in animation)
- Personalised greeting using the user's name from `AppViewModel`
- A tips card with three rows (mic, family, stories)
- "Start Remembering" CTA

On CTA tap:
```kotlin
appVM.completeOnboarding()  // sets isOnboardingComplete = true
onDone()                    // MainActivity rerenders → shows main app
```

---

### `ui/home/MainAppPlaceholderScreen.kt`

Shown after onboarding until the real main app is built in v0.4. Displays the user's name, a "coming in v0.4" message, and a dev-only "Reset onboarding" button (calls `appVM.resetToNewUser()`).

**Replace this entire file in v0.4** with `MainAppScreen.kt` containing the tab scaffold.

---

## 6. Localisation system

The app uses a custom string registry instead of Android's `strings.xml` because:
- Android's localisation requires an Activity restart to switch languages
- Welcome Back must switch languages instantly (user picks Finnish in onboarding, all text updates immediately)

**How it works:**

```kotlin
// Strings.kt — two maps
object Strings {
    val en = mapOf("onboarding.welcome.title" to "Welcome Back", ...)
    val fi = mapOf("onboarding.welcome.title" to "Tervetuloa takaisin", ...)
}

// LanguageManager.t() — the lookup
fun t(key: String): String =
    when (language) {
        ENGLISH -> en[key] ?: key           // fallback to key if missing
        FINNISH -> fi[key] ?: en[key] ?: key // fallback fi → en → key
    }
```

**In a Composable:**
```kotlin
val lang = LanguageManager
Text(text = lang.t("onboarding.welcome.title"))
// Recomposes automatically when LanguageManager.language changes
```

**Adding a new string:**
1. Add the key and English text to `Strings.en`
2. Add the Finnish translation to `Strings.fi`
3. Use `lang.t("your.new.key")` in the Composable

**Format strings** (for strings with variables):
```kotlin
// In Strings.kt:
"onboarding.complete.title" to "You're all set%s!"

// In the Composable:
lang.t("onboarding.complete.title", if (name.isBlank()) "" else ", $name")
// Result: "You're all set, Harri!" or "You're all set!"
```

Use `%s` for strings and `%d` for integers. These are Java `String.format` specifiers.

---

## 7. Roadmap

Each section below describes exactly what needs to be built, where, and how to integrate it with the existing code.

---

### v0.3 — On-device AI (Gemma 4)

**Goal:** download the Gemma 4 model to the device during onboarding so the main conversation in v0.4 can run entirely offline.

**Why on-device?** The core privacy promise is "your data stays on your phone". An on-device LLM means even the conversation transcripts never leave the device.

#### Dependencies

Already declared in `app/build.gradle.kts`:
```kotlin
implementation("com.google.mediapipe:tasks-genai:0.10.16")
```

#### What to build

**1. `services/GemmaService.kt`** — new file

```kotlin
object GemmaService {
    private const val MODEL_DIR = "gemma4"
    private const val MODEL_FILENAME = "gemma-4-it-q4_k_m.bin"  // confirm exact name from MediaPipe docs

    fun isModelDownloaded(context: Context): Boolean {
        return File(context.filesDir, "$MODEL_DIR/$MODEL_FILENAME").exists()
    }

    suspend fun downloadModel(context: Context, onProgress: (Float) -> Unit) {
        // Download from the HuggingFace / Google URL
        // Save to context.filesDir/gemma4/
        // Call onProgress(0f..1f) for the progress bar
    }

    fun createSession(context: Context): LlmInference {
        val modelPath = File(context.filesDir, "$MODEL_DIR/$MODEL_FILENAME").absolutePath
        val options = LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelPath)
            .setMaxTokens(1024)
            .build()
        return LlmInference.createFromOptions(context, options)
    }

    suspend fun respond(session: LlmInference, prompt: String): String {
        return suspendCoroutine { cont ->
            session.generateResponseAsync(prompt) { response, done ->
                if (done) cont.resume(response ?: "")
            }
        }
    }
}
```

**2. Update `OnboardingModelDownloadScreen.kt`**

Replace the "advances immediately" stub with real download logic:
- Show a `LinearProgressIndicator` driven by `downloadProgress` state
- Call `GemmaService.downloadModel(context) { progress -> downloadProgress = progress }`
- Only show "Continue" button once `isModelDownloaded` is true (or offer a genuine "Skip — use cloud" path)

**3. Update `AppViewModel.kt`**

Add a `val isModelReady: StateFlow<Boolean>` that checks `GemmaService.isModelDownloaded()` on init.

**4. Create `services/ConversationService.kt`** (prep for v0.4)

This will hold the `LlmInference` session singleton and the chat history. Create the file now with a stub:
```kotlin
object ConversationService {
    private var session: LlmInference? = null

    fun init(context: Context) {
        if (GemmaService.isModelDownloaded(context)) {
            session = GemmaService.createSession(context)
        }
    }

    suspend fun chat(message: String, profile: UserProfile, language: AppLanguage): String {
        // Build system prompt from UserProfile
        // Call session?.respond() or GeminiService.chat() as fallback
        TODO("Implement in v0.4")
    }
}
```

#### Checklist
- [ ] `GemmaService.kt` created with download + inference API
- [ ] `OnboardingModelDownloadScreen` shows real download progress
- [ ] `AppViewModel` exposes `isModelReady`
- [ ] `ConversationService.kt` stub created
- [ ] Tested on a real device (emulator does not support GPU delegates)
- [ ] CHANGELOG.md updated
- [ ] GitHub release v0.3.0

---

### v0.4 — Main app UI

**Goal:** build the four-tab main app that users see after onboarding. This is the bulk of the user experience.

#### Tab structure

```
MainAppScreen
├── HomeTab          — mic button, conversation transcript, family faces strip
├── MemoriesTab      — scrollable list of memories and stories
├── FamilyTab        — grid/list of family members with photos
└── SettingsTab      — name, photo, voice mode, language, notifications, reset
```

#### What to build

**1. `ui/home/MainAppScreen.kt`** — replace `MainAppPlaceholderScreen`

```kotlin
@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { WelcomeBackBottomBar(navController) }
    ) { padding ->
        NavHost(navController, startDestination = "home") {
            composable("home")      { HomeTab() }
            composable("memories")  { MemoriesTab() }
            composable("family")    { FamilyTab() }
            composable("settings")  { SettingsTab() }
        }
    }
}
```

**2. `ui/home/HomeTab.kt`** — the main conversation screen

Key elements:
- Large mic button (FAB style) that toggles speech recognition
- Conversation transcript — a `LazyColumn` of `UserMessage` / `AssistantMessage` bubbles
- Family faces strip at the top — horizontal `LazyRow` of circular profile photos
- "Good morning, [name]" greeting based on time of day

The mic button should:
1. Request `RECORD_AUDIO` permission if not granted
2. Start `SpeechRecognizer` (see v0.5)
3. When recognition result arrives, call `ConversationService.chat(message, profile, language)`
4. Append both messages to a local `conversationHistory: List<ChatMessage>` state
5. Speak the response via TTS (see v0.5)

For v0.4, you can use a fake/stubbed response from `ConversationService` and skip TTS — just show the text.

**3. `ui/home/MemoriesTab.kt`**

A `LazyColumn` showing the user's memories grouped by `MemoryCategory`. Tap a memory to open a detail sheet. The detail sheet reads the memory description aloud (v0.5).

**4. `ui/home/FamilyTab.kt`**

A `LazyVerticalGrid` (2 columns) of family members. Each card shows:
- The family member's photo (Coil `AsyncImage`, resolving the `photo:` token via `PhotoStorage.fileFor()`)
- Name and relationship

Tap to open a detail screen with the full bio and memory text.

**5. `ui/settings/SettingsTab.kt`**

Settings the user can change:
- Profile name and photo
- Language (EN/FI) — calls `LanguageManager.setLanguage()`
- Voice mode (cloud / local) — updates `UserProfile.preferredVoiceMode`
- Add family member (opens the same form as onboarding)
- Add place
- Add story
- Reset app (dev / carer use)

**6. Update `AppViewModel.kt`**

Add mutation methods for Settings:
```kotlin
fun updateBiography(bio: String) = update { it.copy(biography = bio) }
fun removeFamilyMember(id: String) = update { it.copy(familyMembers = it.familyMembers.filter { m -> m.id != id }) }
fun updateFamilyMember(member: FamilyMember) = update {
    it.copy(familyMembers = it.familyMembers.map { m -> if (m.id == member.id) member else m })
}
// Similar for places and memories
```

**7. Update `MainActivity.kt`**

Replace `MainAppPlaceholderScreen` with `MainAppScreen`.

#### Checklist
- [ ] `MainAppScreen.kt` with `NavHost` and `BottomBar`
- [ ] `HomeTab.kt` (stub conversation, no voice yet)
- [ ] `MemoriesTab.kt`
- [ ] `FamilyTab.kt`
- [ ] `SettingsTab.kt` with all settings
- [ ] `AppViewModel` mutation methods for edit/delete
- [ ] `MainActivity` updated
- [ ] CHANGELOG.md updated
- [ ] GitHub release v0.4.0

---

### v0.5 — Voice and Music

**Goal:** make the app speak and listen — the most important part of the experience.

#### Text-to-Speech (Google Cloud TTS)

**Create `services/TTSService.kt`**

```kotlin
object TTSService {
    private const val ENDPOINT = "https://texttospeech.googleapis.com/v1/text:synthesize"

    // voice: "fi-FI-Wavenet-A" for Finnish, "en-GB-Wavenet-B" for English (warm, older voice)
    suspend fun speak(
        text: String,
        language: AppLanguage,
        context: Context
    ) {
        val apiKey = BuildConfig.GOOGLE_TTS_API_KEY
        if (apiKey.isBlank()) {
            // Fallback to Android's built-in TTS
            fallbackTts(text, context)
            return
        }
        // POST to Google TTS REST API
        // Decode base64 audioContent → ByteArray
        // Play via AudioTrack or MediaPlayer
    }
}
```

**Recommended voices:**
- Finnish: `fi-FI-Wavenet-A` (female, natural) or `fi-FI-Standard-A`
- English: `en-GB-Wavenet-B` (male, warm) — British English sounds more natural for an elder-care app

⚠️ **Privacy:** Google Cloud TTS sends the text to Google servers for synthesis. The privacy policy must disclose this. Consider adding a "Voice off" option for users who prefer silence.

#### Speech Recognition

**Create `services/SpeechService.kt`**

```kotlin
class SpeechService(private val context: Context) {
    private var recognizer: SpeechRecognizer? = null
    private var intent: Intent? = null

    fun startListening(
        language: AppLanguage,
        onResult: (String) -> Unit,
        onError: (Int) -> Unit
    ) {
        val languageCode = if (language == AppLanguage.FINNISH) "fi-FI" else "en-US"
        intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        }
        recognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onResults(results: Bundle?) {
                    val text = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull() ?: ""
                    onResult(text)
                }
                override fun onError(error: Int) = onError(error)
                // Implement remaining abstract methods as no-ops
            })
            startListening(intent)
        }
    }

    fun stopListening() = recognizer?.stopListening()
    fun destroy() = recognizer?.destroy()
}
```

#### Spotify integration

**Dependencies** (in `app/build.gradle.kts` — uncomment when ready):
```kotlin
implementation("com.spotify.android:auth:2.1.0")
// Check https://developer.spotify.com/documentation/android for current coordinates
```

**Create `services/SpotifyService.kt`**

```kotlin
object SpotifyService {
    fun connect(activity: Activity, clientId: String, redirectUri: String) {
        // Use Spotify Auth SDK to get an access token
        // Store token in DataStore / EncryptedSharedPreferences
    }

    fun play(trackUri: String) {
        // Connect to Spotify App Remote and play
    }

    fun playPlaylist(playlistUri: String) { ... }
}
```

**Update `OnboardingMusicScreen.kt`:** replace the stub `onClick = onContinue` with the real Spotify auth flow. After auth completes, call `onContinue`.

#### Checklist
- [ ] `TTSService.kt` with Google Cloud TTS + Android TTS fallback
- [ ] `SpeechService.kt` wrapping `SpeechRecognizer`
- [ ] `HomeTab` mic button wired to real STT + TTS
- [ ] `SpotifyService.kt` with auth and playback
- [ ] `OnboardingMusicScreen` wired to Spotify auth
- [ ] RECORD_AUDIO permission request flow in `HomeTab`
- [ ] CHANGELOG.md updated
- [ ] GitHub release v0.5.0

---

### v0.6 — Maps, notifications, polish

**Goal:** add location context, gentle reminder notifications, and visual polish.

#### Google Maps

**Dependency** (already declared):
```kotlin
implementation("com.google.maps.android:maps-compose:6.1.2")
```

**Create `ui/places/PlacesMapScreen.kt`**

Shows a `GoogleMap` composable with markers for each `Place` in `userProfile.places`. Tap a marker to show the place name and description in a bottom sheet.

**MAPS_API_KEY** must be set in `local.properties`. It is injected into `AndroidManifest.xml` via `manifestPlaceholders` in `build.gradle.kts`.

#### Notifications

**Permission:** `POST_NOTIFICATIONS` is already in `AndroidManifest.xml`. On Android 13+ you must also request it at runtime.

**Use cases:**
- Morning greeting: "Good morning, [name]! Your daughter Maria is coming to visit today."
- Gentle reminders: "It's time to listen to your favourite music."

Implement with `WorkManager` for reliable scheduled notifications (survives device restarts).

#### Accessibility and polish

- Increase minimum touch target to 48 dp (already the case for most components)
- Add content descriptions to all `Icon` and `AsyncImage` composables
- Test with TalkBack enabled
- Increase font size on the Home screen for low-vision users
- Review and finalise Finnish translations with a native speaker

#### Checklist
- [ ] `PlacesMapScreen.kt` with map and markers
- [ ] Maps tab or integration in Memories tab
- [ ] `WorkManager` notification service
- [ ] Runtime `POST_NOTIFICATIONS` permission request
- [ ] Full TalkBack accessibility pass
- [ ] Content descriptions on all images and icons
- [ ] Finnish translations reviewed by native speaker
- [ ] CHANGELOG.md updated
- [ ] GitHub release v0.6.0

---

### v1.0 — Google Play release

**Goal:** publish to Google Play internal testing track.

#### Required before submission

**1. Signed release APK / AAB**

In Android Studio: Build → Generate Signed Bundle / APK → Android App Bundle.  
Create a keystore and store it securely (outside the repo). Add keystore config to `app/build.gradle.kts`:
```kotlin
signingConfigs {
    create("release") {
        storeFile = file("../keystore/welcomeback.jks")
        storePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
        keyAlias = "welcomeback"
        keyPassword = System.getenv("KEY_PASSWORD") ?: ""
    }
}
```

**Never commit the keystore file or passwords to git.**

**2. Privacy policy**

Required by Google Play. Must disclose:
- What data is collected (name, photo, family details, memories — all stored locally)
- That Google Cloud TTS sends synthesised text to Google
- That Gemini API sends memory hint text to Google
- No account creation, no cloud sync, no advertising

**3. App content rating**

Complete the Play Store content rating questionnaire. Expected rating: **Everyone** (PEGI 3 / ESRB Everyone).

**4. Store listing**

- App name: "Welcome Back"
- Short description: "A compassionate AI companion for people with memory challenges."
- Full description: explain the app's purpose, features, and privacy approach
- Screenshots: at least 2 phone screenshots
- Feature graphic (1024 × 500 px)
- App icon: 512 × 512 px (export from `splash_icon.xml`)

**5. Minimum SDK**

Currently `minSdk = 26` (Android 8.0). This covers ~97% of active Android devices as of 2025. Do not raise it without checking current market share data.

**6. Version bump**

In `app/build.gradle.kts`:
```kotlin
versionCode = 10   // increment by 1 for every Play Store upload
versionName = "1.0.0"
```

#### Checklist
- [ ] Signed release AAB generated and tested
- [ ] Keystore secured (password manager, not in repo)
- [ ] Privacy policy published at a stable URL
- [ ] Content rating questionnaire completed
- [ ] Store listing text and screenshots prepared
- [ ] `versionCode` and `versionName` updated
- [ ] Internal testing track created in Play Console
- [ ] AAB uploaded and internal testing link shared with testers
- [ ] CHANGELOG.md updated
- [ ] GitHub release v1.0.0

---

## 8. Patterns and conventions

### Composable structure

Every screen follows this layout:

```kotlin
@Composable
fun ExampleScreen(onContinue: () -> Unit) {
    val lang = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsState()
    val scope = rememberCoroutineScope()

    // Local UI state
    var inputValue by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp)
    ) {
        Spacer(Modifier.height(56.dp))  // or weight(1f) to centre vertically
        // ... content ...
        OnboardingPrimaryButton(text = lang.t("screen.cta"), onClick = { ... })
        OnboardingSkipLink(text = lang.t("screen.skip"), onClick = onContinue)
        Spacer(Modifier.height(48.dp))
    }
}
```

### AI expansion pattern

Every screen with an AI expand button follows the same pattern:

```kotlin
var isGenerating by remember { mutableStateOf(false) }
var aiError by remember { mutableStateOf<String?>(null) }

Button(
    onClick = {
        if (text.isNotBlank() && !isGenerating) {
            isGenerating = true
            aiError = null
            scope.launch {
                try {
                    text = GeminiService.expandMemory(
                        hint = text,
                        userName = profile.name,
                        language = lang.language
                    )
                } catch (_: Throwable) {
                    aiError = lang.t("onboarding.ai.error")
                } finally {
                    isGenerating = false
                }
            }
        }
    },
    enabled = text.isNotBlank() && !isGenerating,
    // ...
)
```

### Release process

```bash
# 1. Edit CHANGELOG.md — move items from [Unreleased] to a new version section
# 2. Bump versionCode and versionName in app/build.gradle.kts
# 3. Commit all changes
git add -A
git commit -m "vX.Y: <one-line summary>"
# 4. Tag and push
git tag vX.Y.0
git push origin main --tags
# 5. Create GitHub release with release notes from CHANGELOG
gh release create vX.Y.0 --title "vX.Y.0 – <title>" --notes "..."
```

### String keys

Always use dot-notation: `<scope>.<screen>.<element>.<variant>`.

```
onboarding.welcome.title     → Welcome screen, title text
onboarding.family.ai.button  → Family screen, AI button label
settings.language.title      → Settings screen, language section title
home.mic.label               → Home screen, mic button accessibility label
```

---

## 9. Known issues and constraints

### No Gradle command-line wrapper on new machines

The `gradlew` wrapper script is in the repo, but it requires Android Studio to have set up the Gradle distribution at least once. If you get a "Gradle not found" error on a fresh clone, open the project in Android Studio first.

### Spotify SDK coordinates unconfirmed

The Spotify SDK dependency is commented out in `build.gradle.kts` with a note. Before implementing v0.5 Spotify features, find the current JitPack / Maven coordinates from the [Spotify developer documentation](https://developer.spotify.com/documentation/android). The package names used in `SpotifyService` may need updating.

### Finnish grammar in dynamic strings

Some strings are hard to translate into Finnish because Finnish uses grammatical cases (genitive, partitive) that change the ending of words based on context. For example, "your daughter Maria's memory" requires the genitive form of "Maria" → "Marian". This cannot be done with simple `%s` substitution.

**Workaround options:**
- Use a simpler phrasing that avoids inflection (e.g., "Maria — memory" instead of "Maria's memory")
- Add a `toFinnishGenitive(name: String): String` helper that appends the correct suffix based on the last character of the name (there are rules for this in Finnish)
- Accept the English wording for some edge-case strings

### On-device AI (Gemma 4) requires a real device

The MediaPipe LLM Inference API uses GPU delegates. Android emulators do not support hardware GPU acceleration for ML tasks. **Always test on-device AI features on a physical Android device.**

### Google Cloud TTS is a paid service

Google Cloud TTS has a free tier (1 million characters/month for standard voices, 1 million characters/month for WaveNet). Above the free tier it costs ~$4–16 per million characters. Monitor usage in the Google Cloud Console.

---

## 10. iOS parity reference

The Android app is a port of the iOS app. When the iOS app adds a feature, check this table to understand the Android equivalent.

| iOS concept | Android equivalent |
|-------------|-------------------|
| `SwiftUI View` | `@Composable fun` |
| `@StateObject / @EnvironmentObject` | `viewModel()` + `collectAsState()` |
| `AppViewModel (ObservableObject)` | `AppViewModel (AndroidViewModel)` |
| `@Published var` | `MutableStateFlow` + `StateFlow` |
| `UserDefaults` / `Codable` | DataStore + `kotlinx.serialization` |
| `URLSession` async/await | Ktor `HttpClient` + `suspend fun` |
| `AVSpeechSynthesizer` | `android.speech.tts.TextToSpeech` or Google Cloud TTS |
| `SFSpeechRecognizer` | `android.speech.SpeechRecognizer` |
| `MPMediaLibrary` / Apple Music | Spotify Android SDK |
| `MapKit` | Google Maps SDK / maps-compose |
| `PhotosPicker` | `PickVisualMedia` ActivityResultContract |
| `%@` format specifier | `%s` |
| `Bundle.main.infoDictionary["KEY"]` | `BuildConfig.KEY` |
| `UIColor(named:)` | `Color(0xFFRRGGBB)` in `Color.kt` |
| `@AppStorage` | `DataStore` via `UserProfileRepository` |

**iOS repo:** https://github.com/juntunen-ai/welcome-back  
When in doubt about intended behaviour, read the iOS source code — it is the reference implementation.
