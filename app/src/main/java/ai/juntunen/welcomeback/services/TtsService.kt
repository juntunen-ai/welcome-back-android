package ai.juntunen.welcomeback.services

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import ai.juntunen.welcomeback.AppLanguage
import ai.juntunen.welcomeback.LanguageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

/**
 * Android TTS wrapper — mirrors iOS `SpeechService` TTS path.
 * Uses Android's built-in `TextToSpeech` engine. Call `init()` before speaking,
 * and `release()` when the owning composable/VM is destroyed.
 *
 * Speech rate 0.9f corresponds roughly to iOS AVSpeechSynthesizer rate 0.48
 * (different scales; adjust empirically).
 */
class TtsService(private val context: Context) {

    private var tts: TextToSpeech? = null
    private var isReady = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                applyLanguage()
                tts?.setSpeechRate(0.9f)
                tts?.setPitch(0.95f)
                isReady = true
            }
        }
    }

    private fun applyLanguage() {
        val locale = when (LanguageManager.language) {
            AppLanguage.FINNISH -> Locale("fi", "FI")
            AppLanguage.ENGLISH -> Locale.UK  // British English — warmer for elder-care context
        }
        tts?.language = locale
    }

    /** Speak [text] and return when finished. */
    suspend fun speak(text: String) {
        if (!isReady || text.isBlank()) return
        applyLanguage()

        return suspendCancellableCoroutine { cont ->
            val utteranceId = "wb_${System.currentTimeMillis()}"
            _isPlaying.value = true

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(id: String?) {}
                override fun onDone(id: String?) {
                    _isPlaying.value = false
                    if (cont.isActive) cont.resume(Unit)
                }
                override fun onError(id: String?) {
                    _isPlaying.value = false
                    if (cont.isActive) cont.resume(Unit)
                }
            })

            val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
            if (result == TextToSpeech.ERROR) {
                _isPlaying.value = false
                if (cont.isActive) cont.resume(Unit)
            }

            cont.invokeOnCancellation { stop() }
        }
    }

    /** Speak without suspending (fire and forget). */
    fun speakAsync(text: String) {
        if (!isReady || text.isBlank()) return
        applyLanguage()
        _isPlaying.value = true
        val utteranceId = "wb_${System.currentTimeMillis()}"
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(id: String?) {}
            override fun onDone(id: String?) { _isPlaying.value = false }
            override fun onError(id: String?) { _isPlaying.value = false }
        })
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun stop() {
        tts?.stop()
        _isPlaying.value = false
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isReady = false
        _isPlaying.value = false
    }
}
