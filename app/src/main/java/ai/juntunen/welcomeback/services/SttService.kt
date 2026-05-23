package ai.juntunen.welcomeback.services

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import ai.juntunen.welcomeback.AppLanguage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Android SpeechRecognizer wrapper — mirrors iOS `SpeechService` STT path.
 * Must be created and destroyed on the Main thread.
 * Call `startListening()` to begin, and `destroy()` when done.
 */
class SttService(private val context: Context) {

    private var recognizer: SpeechRecognizer? = null

    private val _transcriptFlow = MutableStateFlow("")
    val transcriptFlow: StateFlow<String> = _transcriptFlow.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _finalResultFlow = MutableSharedFlow<String>(extraBufferCapacity = 8)
    val finalResultFlow: SharedFlow<String> = _finalResultFlow.asSharedFlow()

    private val _errorFlow = MutableSharedFlow<String>(extraBufferCapacity = 8)
    val errorFlow: SharedFlow<String> = _errorFlow.asSharedFlow()

    fun startListening(language: AppLanguage) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _errorFlow.tryEmit("Speech recognition not available on this device")
            return
        }

        recognizer?.destroy()
        recognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) { _isListening.value = true }
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() { _isListening.value = false }

                override fun onResults(results: Bundle?) {
                    val texts = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = texts?.firstOrNull() ?: ""
                    _transcriptFlow.value = text
                    _finalResultFlow.tryEmit(text)
                    _isListening.value = false
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val texts = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val partial = texts?.firstOrNull() ?: ""
                    if (partial.isNotBlank()) _transcriptFlow.value = partial
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}

                override fun onError(error: Int) {
                    _isListening.value = false
                    val msg = when (error) {
                        SpeechRecognizer.ERROR_AUDIO              -> "Audio recording error"
                        SpeechRecognizer.ERROR_CLIENT             -> "Client error"
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission denied"
                        SpeechRecognizer.ERROR_NETWORK            -> "Network error"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT    -> "Network timeout"
                        SpeechRecognizer.ERROR_NO_MATCH           -> "No speech detected"
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY    -> "Recogniser busy"
                        SpeechRecognizer.ERROR_SERVER             -> "Server error"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT     -> "No speech input"
                        else                                       -> "Recognition error $error"
                    }
                    // NO_MATCH / SPEECH_TIMEOUT are normal — only propagate real errors
                    if (error != SpeechRecognizer.ERROR_NO_MATCH &&
                        error != SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
                        _errorFlow.tryEmit(msg)
                    }
                }
            })
        }

        val languageTag = when (language) {
            AppLanguage.FINNISH -> "fi-FI"
            AppLanguage.ENGLISH -> "en-US"
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageTag)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)  // prefer on-device like iOS
        }

        _transcriptFlow.value = ""
        recognizer?.startListening(intent)
    }

    fun stopListening() {
        recognizer?.stopListening()
        _isListening.value = false
    }

    fun destroy() {
        recognizer?.destroy()
        recognizer = null
        _isListening.value = false
    }
}
