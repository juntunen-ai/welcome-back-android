import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
}

// Read local.properties at the top level so the keys are available everywhere
val localPropsFile = rootProject.file("local.properties")
val localProperties = Properties().apply {
    if (localPropsFile.exists()) localPropsFile.inputStream().use { load(it) }
}
val geminiApiKey    = localProperties.getProperty("GEMINI_API_KEY",    "")
val googleTtsApiKey = localProperties.getProperty("GOOGLE_TTS_API_KEY", "")
val mapsApiKey      = localProperties.getProperty("MAPS_API_KEY",      "")

android {
    namespace = "ai.juntunen.welcomeback"
    compileSdk = 35

    defaultConfig {
        applicationId = "ai.juntunen.welcomeback"
        minSdk = 26          // Android 8.0 — covers ~97% of active devices
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        vectorDrawables.useSupportLibrary = true

        buildConfigField("String", "GEMINI_API_KEY",    "\"$geminiApiKey\"")
        buildConfigField("String", "GOOGLE_TTS_API_KEY","\"$googleTtsApiKey\"")
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android + Compose BOM
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")

    val composeBom = platform("androidx.compose:compose-bom:2024.09.03")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Compose Material 3
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.1")

    // DataStore (preferences + profile JSON)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Room (local database)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Networking — Ktor for Gemini + Google TTS REST APIs
    implementation("io.ktor:ktor-client-core:2.3.12")
    implementation("io.ktor:ktor-client-okhttp:2.3.12")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")

    // Kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Google Maps + Location
    implementation("com.google.maps.android:maps-compose:6.1.2")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // WorkManager for scheduled notifications
    implementation("androidx.work:work-runtime-ktx:2.9.1")

    // Accompanist permissions helper
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    // MediaPipe Tasks GenAI (Gemma on-device inference)
    implementation("com.google.mediapipe:tasks-genai:0.10.16")

    // Spotify Android SDK — wired up in v0.5; keep commented until then so
    // the project builds without a JitPack roundtrip for new contributors.
    // implementation("com.spotify.android:auth:2.1.0")
    // implementation("com.spotify.android:spotify-app-remote:0.8.0")

    // Splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
