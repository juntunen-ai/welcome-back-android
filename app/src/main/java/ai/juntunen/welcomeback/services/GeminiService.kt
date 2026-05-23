package ai.juntunen.welcomeback.services

import ai.juntunen.welcomeback.AppLanguage
import ai.juntunen.welcomeback.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Communicates with the Gemini REST API to expand brief memory hints into
 * warm 2-3 sentence personal memories. Mirrors iOS `GeminiService.expandMemory`.
 *
 * When [BuildConfig.GEMINI_API_KEY] is empty (no key configured in
 * local.properties) the service silently returns the hint unchanged, so the
 * app remains usable offline / for developers without a key.
 */
object GeminiService {

    private const val MODEL = "gemini-2.0-flash"
    private val ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent"

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) { json(this@GeminiService.json) }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 15_000
            socketTimeoutMillis  = 30_000
        }
    }

    @Serializable
    private data class Part(val text: String)
    @Serializable
    private data class Content(val parts: List<Part>)
    @Serializable
    private data class GenConfig(
        val temperature: Double = 0.75,
        val topP: Double = 0.85,
        val maxOutputTokens: Int = 250
    )
    @Serializable
    private data class Request(
        val contents: List<Content>,
        val generationConfig: GenConfig = GenConfig()
    )
    @Serializable
    private data class Candidate(val content: Content)
    @Serializable
    private data class Response(val candidates: List<Candidate> = emptyList())

    suspend fun expandMemory(
        hint: String,
        userName: String,
        language: AppLanguage
    ): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || hint.isBlank()) return hint

        val prompt = when (language) {
            AppLanguage.FINNISH -> """
                Laajenna tämä lyhyt muistokuvaus lämpimäksi, henkilökohtaiseksi 2–3 lauseen muistokirjoitukseksi suomeksi. \
                Kirjoita ensimmäisessä persoonassa kuin muistelisit omaa muistoa. Käytä lämmintä, tunteikasta kieltä. \
                Älä lisää selityksiä tai otsikoita — kirjoita ainoastaan itse muistokertomus.

                Kuvaus: $hint
            """.trimIndent()
            AppLanguage.ENGLISH -> """
                Expand this brief memory description into a warm, personal 2–3 sentence memory story in English. \
                Write in first person as if recalling your own memory. Use warm, emotional language. \
                Return only the memory text — no headings or explanations.

                Description: $hint
            """.trimIndent()
        }

        val response: Response = client.post(ENDPOINT) {
            header("x-goog-api-key", apiKey)
            contentType(ContentType.Application.Json)
            setBody(Request(contents = listOf(Content(parts = listOf(Part(prompt))))))
        }.body()

        val text = response.candidates
            .firstOrNull()?.content?.parts
            ?.firstOrNull()?.text
            ?.trim()
            .orEmpty()

        return text.ifBlank { hint }
    }
}
