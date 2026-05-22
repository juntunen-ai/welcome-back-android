package ai.juntunen.welcomeback

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.content.edit

enum class AppLanguage(val code: String, val displayName: String, val flag: String) {
    ENGLISH("en", "English", "🇬🇧"),
    FINNISH("fi", "Suomi",   "🇫🇮")
}

/**
 * Runtime-swappable language registry. Mirrors `LanguageManager.swift` from iOS:
 * the user picks English or Finnish in onboarding, can change it in Settings,
 * and every screen recomposes immediately because [language] is observable
 * Compose state.
 */
object LanguageManager {

    private const val PREFS = "welcomeback.language"
    private const val KEY   = "app_language"

    private val _language = mutableStateOf(AppLanguage.ENGLISH)
    val language: AppLanguage get() = _language.value

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
        val saved = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY, null)
        _language.value = AppLanguage.entries.firstOrNull { it.code == saved }
            ?: AppLanguage.ENGLISH
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
        appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit { putString(KEY, lang.code) }
    }

    /** Returns the localised string for [key]. Falls back to English, then to the key. */
    fun t(key: String): String =
        when (language) {
            AppLanguage.ENGLISH -> Strings.en[key] ?: key
            AppLanguage.FINNISH -> Strings.fi[key] ?: Strings.en[key] ?: key
        }

    fun t(key: String, vararg args: Any): String =
        try { t(key).format(*args) } catch (_: Throwable) { t(key) }
}

/** Convenience Compose accessor — read inside any @Composable to recompose on language change. */
val LocalLanguage = staticCompositionLocalOf { LanguageManager }
