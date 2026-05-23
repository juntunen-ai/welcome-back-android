# Welcome Back — Android

Compassionate AI companion for memory-challenged individuals.  
Android version of the [iOS app](https://github.com/juntunen-ai/welcome-back).

> **For a full developer guide** — architecture, every file explained, and step-by-step instructions for every roadmap phase — see **[DEVELOPER.md](DEVELOPER.md)**.

---

## What is Welcome Back?

Welcome Back is an app designed for people living with memory challenges (dementia, Alzheimer's, age-related memory loss) and their families. The app acts as a gentle AI companion that:

- **Remembers who the user is** — name, photo, family members, favourite places, life stories
- **Talks with the user** — voice conversations using speech recognition and text-to-speech
- **Plays familiar music** — Spotify integration to hear the songs they love
- **Shows faces and stories** — family members with photos and personalised bios read aloud
- **Helps caregivers** — family members can add content remotely (coming in later versions)

The guiding design principle is **calm, large, unhurried UI** and **data stays on the device** — privacy is paramount.

---

## Status

| Version | Status | What's in it |
|---------|--------|--------------|
| v0.1 | ✅ Released | Project skeleton, dark theme, language manager (EN/FI), Welcome + Language screens |
| **v0.2** | ✅ **Released** | Full 13-step onboarding, AppViewModel, DataStore persistence, Gemini AI expansion |
| v0.3 | 🔜 Next | MediaPipe + Gemma 4 on-device model download and inference |
| v0.4 | Planned | Main app — Home conversation mic, Memories, Family, Settings |
| v0.5 | Planned | Google Cloud TTS + Spotify SDK + SpeechRecognizer |
| v0.6 | Planned | Google Maps, notifications, animations, accessibility |
| v1.0 | Planned | Google Play internal testing release |

---

## Quick start

### Prerequisites

- Android Studio Ladybug (2024.2.1) or newer
- JDK 17
- Android SDK 35
- Device or emulator running Android 8.0 (API 26) or later

### Configure API keys

```bash
cp local.properties.example local.properties
# Edit local.properties and fill in your keys
```

Minimum for a working build (cloud AI features disabled if blank):

```properties
sdk.dir=/Users/YOU/Library/Android/sdk
GEMINI_API_KEY=your-gemini-key      # https://aistudio.google.com/app/apikey
```

Keys are injected as `BuildConfig` constants at build time and are **never committed to git**.

### Run

Open in Android Studio → press **Run ▶︎**.

First build downloads Gradle 8.9 and dependencies (~650 MB total). Subsequent builds are fast.

```bash
# Command line (after Android Studio has set up the Gradle wrapper):
./gradlew :app:installDebug
```

---

## Architecture at a glance

**Native Android · Kotlin · Jetpack Compose · Material 3 · MVVM**

| Concern | Solution |
|---------|----------|
| UI | Jetpack Compose + Material 3 |
| State | `AndroidViewModel` + `StateFlow` |
| Persistence | DataStore (profile JSON) + Room (future relational store) |
| Localisation | `LanguageManager` singleton — runtime EN/FI switch, no restart needed |
| Cloud AI | Gemini REST API (`gemini-2.0-flash`) via Ktor |
| On-device AI | MediaPipe LLM Inference + Gemma 4 *(v0.3)* |
| TTS | Google Cloud Text-to-Speech REST *(v0.5)* |
| STT | `android.speech.SpeechRecognizer` *(v0.5)* |
| Music | Spotify Android SDK *(v0.5)* |
| Maps | Google Maps SDK / maps-compose *(v0.6)* |
| Images | Coil |

---

## Project layout

```
app/src/main/java/ai/juntunen/welcomeback/
├── MainActivity.kt              # Entry point — routes onboarding ↔ main app
├── WelcomeBackApp.kt            # Application class — initialises singletons
├── AppViewModel.kt              # Shared state (UserProfile) for all screens
├── LanguageManager.kt           # Runtime EN/FI switcher
├── Strings.kt                   # ~140 EN + FI strings (no strings.xml)
├── data/
│   ├── Models.kt                # FamilyMember, Memory, Place, UserProfile
│   └── UserProfileRepository.kt # DataStore JSON persistence
├── services/
│   ├── GeminiService.kt         # Gemini 2.0 Flash — expandMemory()
│   └── PhotoStorage.kt          # Copies photos to internal storage
└── ui/
    ├── theme/                   # Color.kt · Type.kt · Theme.kt
    ├── onboarding/              # 13 screens + container + shared components
    └── home/                    # MainAppPlaceholderScreen (full app in v0.4)
```

---

## Key design decisions vs. iOS

| Feature | iOS | Android |
|---------|-----|---------|
| Music | Apple Music / MPMediaLibrary | **Spotify SDK** |
| TTS | AVSpeechSynthesizer / Personal Voice | **Google Cloud TTS** ⚠️ cloud |
| Maps | MapKit | **Google Maps SDK** |
| Strings | `Strings.swift` static dict | `Strings.kt` — same pattern, `%s` not `%@` |
| Persistence | UserDefaults / Codable | DataStore + kotlinx.serialization |

⚠️ **Privacy note:** Google Cloud TTS sends synthesised text to Google servers. The iOS "data stays on your phone" promise does not fully apply to Android TTS. The privacy policy must reflect this difference.

---

## Releasing a new version

```bash
# 1. Update CHANGELOG.md — move [Unreleased] items under a dated version header
# 2. Bump versionCode and versionName in app/build.gradle.kts
# 3. Commit, tag, push
git add -A
git commit -m "vX.Y: <summary>"
git tag vX.Y.0
git push origin main --tags
# 4. Create GitHub release
gh release create vX.Y.0 --title "vX.Y.0 – <title>" --notes "..."
```

---

## License

MIT
