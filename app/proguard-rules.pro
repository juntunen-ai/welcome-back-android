# Keep MediaPipe inference engine classes
-keep class com.google.mediapipe.** { *; }

# Keep Spotify SDK
-keep class com.spotify.** { *; }
-keep class com.spotify.protocol.** { *; }

# Kotlin serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
