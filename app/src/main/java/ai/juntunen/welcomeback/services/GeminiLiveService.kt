package ai.juntunen.welcomeback.services

import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import ai.juntunen.welcomeback.AppLanguage
import ai.juntunen.welcomeback.BuildConfig
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.UserProfile
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.*
import okhttp3.*
import java.util.Base64
import java.util.concurrent.TimeUnit

// ── State machine ────────────────────────────────────────────────────────────

sealed class LiveSessionState {
    object Idle : LiveSessionState()
    object Connecting : LiveSessionState()
    object Listening : LiveSessionState()
    object UserSpeaking : LiveSessionState()
    object AiThinking : LiveSessionState()
    object AiSpeaking : LiveSessionState()
    object Interrupted : LiveSessionState()
    object Disconnected : LiveSessionState()
    data class Error(val message: String) : LiveSessionState()
}

// ── Service ──────────────────────────────────────────────────────────────────

class GeminiLiveService(private val context: Context) {

    private val _state = MutableStateFlow<LiveSessionState>(LiveSessionState.Idle)
    val state: StateFlow<LiveSessionState> = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var webSocket: WebSocket? = null
    private var audioRecord: AudioRecord? = null
    private var audioTrack: AudioTrack? = null
    private var sendJob: Job? = null
    private val accumulatedAudio = mutableListOf<Byte>()

    // Audio constants
    private val SAMPLE_RATE_IN  = 16_000   // 16 kHz capture
    private val SAMPLE_RATE_OUT = 24_000   // 24 kHz playback
    private val CHANNEL_IN      = AudioFormat.CHANNEL_IN_MONO
    private val CHANNEL_OUT     = AudioFormat.CHANNEL_OUT_MONO
    private val FORMAT           = AudioFormat.ENCODING_PCM_16BIT

    private val MODEL  = "models/gemini-2.5-flash-native-audio-preview-12-2025"
    private val VOICE  = "Aoede"
    private val WS_URL = "wss://generativelanguage.googleapis.com/ws/google.ai.generativelanguage.v1beta.GenerativeService.BidiGenerateContent"

    // ── Public API ────────────────────────────────────────────────────────────

    suspend fun startSession(profile: UserProfile) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) {
            _state.value = LiveSessionState.Error("No API key configured")
            return
        }

        _state.value = LiveSessionState.Connecting

        val language = LanguageManager.language
        val systemPrompt = GeminiService.buildSystemPrompt(profile, language)

        val url = "$WS_URL?key=$apiKey&alt=sse"

        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)  // WebSocket must not time out
            .build()

        val request = Request.Builder().url(url).build()

        val listener = object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                webSocket = ws
                // Send setup message
                val setup = buildJsonObject {
                    put("setup", buildJsonObject {
                        put("model", MODEL)
                        put("generation_config", buildJsonObject {
                            put("response_modalities", buildJsonArray { add("AUDIO") })
                            put("speech_config", buildJsonObject {
                                put("voice_config", buildJsonObject {
                                    put("prebuilt_voice_config", buildJsonObject {
                                        put("voice_name", VOICE)
                                    })
                                })
                            })
                        })
                        put("system_instruction", buildJsonObject {
                            put("parts", buildJsonArray {
                                add(buildJsonObject { put("text", systemPrompt) })
                            })
                        })
                    })
                }
                ws.send(setup.toString())
            }

            override fun onMessage(ws: WebSocket, text: String) {
                handleServerMessage(text)
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                _state.value = LiveSessionState.Error(t.message ?: "WebSocket error")
                cleanup()
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                if (_state.value !is LiveSessionState.Error) {
                    _state.value = LiveSessionState.Disconnected
                }
                cleanup()
            }
        }

        client.newWebSocket(request, listener)
    }

    fun endSession() {
        webSocket?.close(1000, "User ended session")
        webSocket = null
        cleanup()
        _state.value = LiveSessionState.Disconnected
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    private fun handleServerMessage(text: String) {
        try {
            val json = Json.parseToJsonElement(text).jsonObject

            // setupComplete → start recording
            if (json.containsKey("setupComplete")) {
                _state.value = LiveSessionState.Listening
                startAudioCapture()
                return
            }

            val serverContent = json["serverContent"]?.jsonObject ?: return

            // interrupted
            if (serverContent["interrupted"]?.jsonPrimitive?.booleanOrNull == true) {
                _state.value = LiveSessionState.Interrupted
                accumulatedAudio.clear()
                return
            }

            // modelTurn audio chunks — accumulate
            val modelTurn = serverContent["modelTurn"]?.jsonObject
            val parts = modelTurn?.get("parts")?.jsonArray
            if (parts != null) {
                _state.value = LiveSessionState.AiSpeaking
                for (part in parts) {
                    val inlineData = part.jsonObject["inlineData"]?.jsonObject
                    val b64 = inlineData?.get("data")?.jsonPrimitive?.content
                    if (b64 != null) {
                        val bytes = Base64.getDecoder().decode(b64)
                        accumulatedAudio.addAll(bytes.toList())
                    }
                }
            }

            // turnComplete → flush audio to AudioTrack
            if (serverContent["turnComplete"]?.jsonPrimitive?.booleanOrNull == true) {
                if (accumulatedAudio.isNotEmpty()) {
                    playAccumulatedAudio()
                }
                accumulatedAudio.clear()
                _state.value = LiveSessionState.Listening
            }

        } catch (_: Exception) {
            // Ignore parse errors for non-JSON SSE lines
        }
    }

    private fun startAudioCapture() {
        sendJob = scope.launch {
            val minBuf = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN, CHANNEL_IN, FORMAT)
            val bufSize = maxOf(minBuf, 3200) // ~100ms at 16kHz
            val recorder = try {
                AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE_IN,
                    CHANNEL_IN,
                    FORMAT,
                    bufSize
                )
            } catch (e: Exception) {
                _state.value = LiveSessionState.Error("Microphone error: ${e.message}")
                return@launch
            }
            audioRecord = recorder
            recorder.startRecording()

            val buffer = ShortArray(bufSize / 2)
            while (isActive && webSocket != null) {
                val read = recorder.read(buffer, 0, buffer.size)
                if (read > 0) {
                    // Convert Short array to byte array (little-endian PCM16)
                    val byteBuffer = java.nio.ByteBuffer.allocate(read * 2)
                        .order(java.nio.ByteOrder.LITTLE_ENDIAN)
                    for (i in 0 until read) byteBuffer.putShort(buffer[i])
                    val encoded = Base64.getEncoder().encodeToString(byteBuffer.array())

                    val msg = buildJsonObject {
                        put("realtime_input", buildJsonObject {
                            put("media_chunks", buildJsonArray {
                                add(buildJsonObject {
                                    put("mime_type", "audio/pcm;rate=16000")
                                    put("data", encoded)
                                })
                            })
                        })
                    }
                    webSocket?.send(msg.toString())

                    if (_state.value == LiveSessionState.Listening) {
                        _state.value = LiveSessionState.UserSpeaking
                    }
                }
                delay(50) // yield between reads
            }

            recorder.stop()
            recorder.release()
        }
    }

    private fun playAccumulatedAudio() {
        scope.launch {
            _state.value = LiveSessionState.AiSpeaking
            try {
                val bytes = accumulatedAudio.toByteArray()
                val minBuf = AudioTrack.getMinBufferSize(SAMPLE_RATE_OUT, CHANNEL_OUT, FORMAT)
                val track = AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    SAMPLE_RATE_OUT,
                    CHANNEL_OUT,
                    FORMAT,
                    maxOf(minBuf, bytes.size),
                    AudioTrack.MODE_STATIC
                )
                audioTrack?.stop()
                audioTrack?.release()
                audioTrack = track
                track.write(bytes, 0, bytes.size)
                track.play()
            } catch (e: Exception) {
                // Audio playback errors are non-fatal — just log and continue
            }
        }
    }

    private fun cleanup() {
        sendJob?.cancel()
        sendJob = null
        try {
            audioRecord?.stop()
            audioRecord?.release()
        } catch (_: Exception) {}
        audioRecord = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
        accumulatedAudio.clear()
        scope.cancel()
    }
}
