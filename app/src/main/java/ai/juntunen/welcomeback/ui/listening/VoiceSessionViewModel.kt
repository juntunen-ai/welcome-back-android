package ai.juntunen.welcomeback.ui.listening

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ai.juntunen.welcomeback.AppLanguage
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.UserProfile
import ai.juntunen.welcomeback.data.UserProfileRepository
import ai.juntunen.welcomeback.services.GeminiService
import ai.juntunen.welcomeback.services.SttService
import ai.juntunen.welcomeback.services.TtsService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Drives the full-screen voice conversation UI.
 * Mirrors iOS `VoiceSessionViewModel` — manage STT → Gemini → TTS pipeline.
 */
class VoiceSessionViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context get() = getApplication()
    private val repo = UserProfileRepository(application)

    private var profile: UserProfile = UserProfile.DEFAULT
    private var language: AppLanguage = AppLanguage.ENGLISH

    val sttService = SttService(context)
    val ttsService = TtsService(context)

    private val _phase = MutableStateFlow(Phase.IDLE)
    val phase: StateFlow<Phase> = _phase.asStateFlow()

    private val _transcript = MutableStateFlow("")
    val transcript: StateFlow<String> = _transcript.asStateFlow()

    private val _reply = MutableStateFlow("")
    val reply: StateFlow<String> = _reply.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var listenJob: Job? = null

    enum class Phase { IDLE, LISTENING, THINKING, SPEAKING, ERROR }

    init {
        viewModelScope.launch {
            profile  = repo.load()
            language = LanguageManager.language
        }

        // Forward live transcript
        viewModelScope.launch {
            sttService.transcriptFlow.collect { _transcript.value = it }
        }

        // When STT produces a final result → send to Gemini
        viewModelScope.launch {
            sttService.finalResultFlow.collect { text ->
                if (text.isNotBlank()) handleFinalTranscript(text)
            }
        }

        // Forward STT errors
        viewModelScope.launch {
            sttService.errorFlow.collect { msg ->
                _phase.value = Phase.ERROR
                _error.value = msg
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────────────────────────

    fun startListening() {
        if (_phase.value == Phase.LISTENING) return
        ttsService.stop()
        _transcript.value = ""
        _reply.value = ""
        _error.value = null
        _phase.value = Phase.LISTENING
        sttService.startListening(language)
    }

    fun stopListening() {
        sttService.stopListening()
        if (_phase.value == Phase.LISTENING) _phase.value = Phase.IDLE
    }

    fun reset() {
        ttsService.stop()
        sttService.stopListening()
        _transcript.value = ""
        _reply.value = ""
        _error.value = null
        _phase.value = Phase.IDLE
    }

    // ─────────────────────────────────────────────────────────────────
    // Internal pipeline
    // ─────────────────────────────────────────────────────────────────

    private fun handleFinalTranscript(text: String) {
        listenJob?.cancel()
        listenJob = viewModelScope.launch {
            _phase.value = Phase.THINKING
            val aiReply = GeminiService.chat(text, profile, language)
            _reply.value = aiReply
            _phase.value = Phase.SPEAKING
            ttsService.speak(aiReply)
            _phase.value = Phase.IDLE
        }
    }

    override fun onCleared() {
        super.onCleared()
        sttService.destroy()
        ttsService.release()
    }
}
