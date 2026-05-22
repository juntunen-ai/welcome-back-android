# Welcome Back — Android

Compassionate AI companion for memory-challenged individuals. Android version of the [iOS app](https://github.com/juntunen-ai/welcome-back).

## Status

🚧 **Early scaffolding** — v0.1 contains the project skeleton, dark theme, language manager (English / Finnish) and the first two onboarding screens (Welcome + Language picker). The remaining 11 onboarding screens and the main app flow are being ported from iOS.

## Architecture

**Native Android, Kotlin + Jetpack Compose, Material 3.**

| Layer | Stack |
|-------|-------|
| UI | Jetpack Compose + Material 3 |
| Navigation | androidx.navigation:navigation-compose |
| State | ViewModel + StateFlow |
| Persistence | DataStore (preferences) + Room (relational) |
| Networking | Ktor (OkHttp engine) |
| Serialization | kotlinx.serialization |
| On-device AI | **MediaPipe LLM Inference** running Gemma 4 |
| Cloud AI | Gemini REST API |
| Speech-to-text | `android.speech.SpeechRecognizer` |
| Text-to-speech | **Google Cloud Text-to-Speech** REST API |
| Maps | Google Maps SDK (maps-compose) |
| Music | Spotify Android SDK + `MediaStore` fallback |
| Image loading | Coil |

## Feature parity with iOS

The Android app is a port of the iOS v1.3 release, which includes:

- ✅ Full English + Finnish localisation, switchable at runtime
- ✅ 13-step guided onboarding (welcome → language → AI model → name → photo → family → place → story → music → done)
- ✅ AI-assisted memory generation during onboarding
- ✅ Home screen with conversation mic
- ✅ Memories tab (People, Places, Stories)
- ✅ Family management
- ✅ Settings with model, voice, notification controls

**Differences from iOS:**
- Music: **Spotify** instead of Apple Music
- TTS: **Google Cloud TTS** instead of iOS AVSpeechSynthesizer / Personal Voice
  - ⚠️ TTS is a cloud service — voice text leaves the device for synthesis. Privacy policy must reflect this.
- Maps: Google Maps instead of MapKit

## Setup

### Prerequisites

- Android Studio Ladybug (2024.2.1) or newer
- JDK 17
- Android SDK 35
- A real device or emulator running Android 8.0 (API 26) or later

### Configure secrets

Copy the example and fill in your API keys:

```bash
cp local.properties.example local.properties
```

Edit `local.properties`:

```properties
sdk.dir=/Users/YOU/Library/Android/sdk

# Required for cloud features
GEMINI_API_KEY=your-gemini-key
GOOGLE_TTS_API_KEY=your-google-tts-key

# Required for the Maps integration
MAPS_API_KEY=your-maps-key
```

These are read at build time and injected as `BuildConfig` constants — they are **not** committed to git.

### Run

Open the project in Android Studio. The first build will download Gradle 8.9 (~150 MB) and all dependencies (~500 MB). Then just press **Run** ▶︎.

From the command line (after Android Studio has set up the wrapper):

```bash
./gradlew :app:installDebug
```

## Project layout

```
app/src/main/java/ai/juntunen/welcomeback/
├── MainActivity.kt              # entry point
├── WelcomeBackApp.kt            # Application class — initialises singletons
├── LanguageManager.kt           # runtime language switcher
├── Strings.kt                   # EN + FI string tables (port of iOS Strings.swift)
├── data/
│   └── Models.kt                # FamilyMember, Memory, Place, UserProfile
├── services/                    # Gemini, Google TTS, MediaPipe LLM, Speech (coming)
└── ui/
    ├── theme/                   # Color, Type, Theme
    ├── onboarding/              # 13-step onboarding flow
    └── home/                    # Home, Memories, Family, Settings (coming)
```

## Roadmap

| Milestone | Scope |
|-----------|-------|
| **v0.1 — Scaffolding** *(current)* | Project + theme + Welcome + Language picker |
| v0.2 — Onboarding | Full 13-step onboarding ported from iOS |
| v0.3 — On-device AI | MediaPipe + Gemma 4 download + inference |
| v0.4 — Main app | Home, mic conversation, Memories tabs, Settings |
| v0.5 — Voice & Music | Google TTS + Spotify SDK + speech recognition |
| v0.6 — Maps & Polish | Maps, notifications, animations |
| v1.0 — Play Store | Internal testing track on Google Play |

## License

MIT
