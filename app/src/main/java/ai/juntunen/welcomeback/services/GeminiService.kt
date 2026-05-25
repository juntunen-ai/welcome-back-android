package ai.juntunen.welcomeback.services

import ai.juntunen.welcomeback.AppLanguage
import ai.juntunen.welcomeback.BuildConfig
import ai.juntunen.welcomeback.data.FamilyMember
import ai.juntunen.welcomeback.data.AIModel
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
 * Communicates with the Gemini REST API.
 * Mirrors iOS `GeminiService` / `GeminiLiveService.buildSystemPrompt`.
 */
object GeminiService {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) { json(this@GeminiService.json) }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 15_000
            socketTimeoutMillis  = 30_000
        }
    }

    // ── Serialisation models ─────────────────────────────────────────────────

    @Serializable
    data class Part(val text: String)

    @Serializable
    data class Content(val role: String = "user", val parts: List<Part>)

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

    // ── System prompt builder (mirrors iOS GeminiLiveService.buildSystemPrompt) ──

    fun buildSystemPrompt(
        profile: ai.juntunen.welcomeback.data.UserProfile,
        language: AppLanguage
    ): String {
        val familySection = if (profile.familyMembers.isNotEmpty()) {
            val lines = profile.familyMembers.joinToString("\n") { m ->
                buildString {
                    append("- ${m.name} (${m.relationship})")
                    if (m.biography.isNotBlank()) append(": ${m.biography}")
                    if (m.memory1.isNotBlank()) append(". Memory: ${m.memory1}")
                    if (m.memory2.isNotBlank()) append(". Also: ${m.memory2}")
                }
            }
            if (language == AppLanguage.FINNISH) "Perheenjäsenet:\n$lines" else "Family members:\n$lines"
        } else ""

        val memoriesSection = if (profile.memories.isNotEmpty()) {
            val lines = profile.memories.joinToString("\n") { m ->
                buildString {
                    append("- ${m.title}")
                    if (m.date.isNotBlank()) append(" (${m.date})")
                    if (m.description.isNotBlank()) append(": ${m.description}")
                }
            }
            if (language == AppLanguage.FINNISH) "Muistot:\n$lines" else "Memories:\n$lines"
        } else ""

        val placesSection = if (profile.places.isNotEmpty()) {
            val lines = profile.places.joinToString("\n") { p ->
                buildString {
                    append("- ${p.name}")
                    if (p.description.isNotBlank()) append(": ${p.description}")
                }
            }
            if (language == AppLanguage.FINNISH) "Tärkeät paikat:\n$lines" else "Important places:\n$lines"
        } else ""

        return when (language) {
            AppLanguage.FINNISH -> """
                Olet Welcome Back, lämminhenkinen ja kärsivällinen tekoälykumppani.
                Tehtäväsi on auttaa käyttäjää muistamaan tärkeitä ihmisiä, paikkoja ja tapahtumia heidän elämässään.
                Käyttäjäsi nimi on ${profile.name.ifBlank { "ystäväsi" }}.
                ${if (profile.biography.isNotBlank()) "Tietoja käyttäjästä: ${profile.biography}" else ""}
                ${if (profile.address.isNotBlank()) "Kotiosoite: ${profile.address}" else ""}
                $familySection
                $memoriesSection
                $placesSection
                Vastaa aina lyhyesti, lämpimästi ja rohkaisevasti suomeksi.
                Älä koskaan käytä otsikoita tai luettelomerkkejä.
                Jos käyttäjä kysyy perheenjäsenestä tai paikasta jota et tunne, ohjaa heitä lempeästi lisäämään se asetuksissa.
            """.trimIndent()

            AppLanguage.ENGLISH -> """
                You are Welcome Back, a warm and patient AI companion.
                Your role is to help the user remember important people, places, and events in their life.
                The user's name is ${profile.name.ifBlank { "your friend" }}.
                ${if (profile.biography.isNotBlank()) "About the user: ${profile.biography}" else ""}
                ${if (profile.address.isNotBlank()) "Home address: ${profile.address}" else ""}
                $familySection
                $memoriesSection
                $placesSection
                Always reply briefly, warmly, and encouragingly in English.
                Never use headings or bullet points.
                If the user asks about a family member or place you don't recognise, gently guide them to add it in Settings.
            """.trimIndent()
        }
    }

    // ── chat() — conversational AI reply ────────────────────────────────────

    suspend fun chat(
        userMessage: String,
        profile: ai.juntunen.welcomeback.data.UserProfile,
        language: AppLanguage,
        history: List<Pair<String, String>> = emptyList()
    ): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || userMessage.isBlank()) {
            return if (language == AppLanguage.FINNISH)
                "Olen täällä sinua varten. Kerro minulle jotain."
            else
                "I'm here for you. Tell me more."
        }

        val modelId = profile.preferredAIModel.apiModel
        val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/$modelId:generateContent"

        val systemPrompt = buildSystemPrompt(profile, language)

        // Build contents: system context first, then history pairs, then new message
        val contents = mutableListOf<Content>()
        contents.add(Content(role = "user", parts = listOf(Part("system: $systemPrompt"))))
        for ((userTurn, modelTurn) in history) {
            contents.add(Content(role = "user",  parts = listOf(Part(userTurn))))
            contents.add(Content(role = "model", parts = listOf(Part(modelTurn))))
        }
        contents.add(Content(role = "user", parts = listOf(Part(userMessage))))

        return try {
            val response: Response = client.post(endpoint) {
                header("x-goog-api-key", apiKey)
                contentType(ContentType.Application.Json)
                setBody(Request(contents = contents, generationConfig = GenConfig(temperature = 0.7, maxOutputTokens = 200)))
            }.body()
            response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text?.trim()
                ?.ifBlank { null }
                ?: if (language == AppLanguage.FINNISH) "Olen täällä sinua varten." else "I'm here for you."
        } catch (_: Throwable) {
            if (language == AppLanguage.FINNISH) "Anteeksi, en juuri nyt pysty vastaamaan."
            else "I'm sorry, I can't respond right now."
        }
    }

    // ── generateMemoryStory() ────────────────────────────────────────────────

    suspend fun generateMemoryStory(
        userName: String,
        member: FamilyMember,
        language: String
    ): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) {
            return "Hi $userName, it's ${member.name}. We are all thinking of you today and love you very much."
        }

        val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"

        val prompt = """
            You are ${member.name}, the ${member.relationship} of ${userName}.
            ${userName} is struggling with memory.
            Speak directly to them as ${member.name}.
            Remind them of who they are and how much they are loved in a short, warm, and comforting paragraph (3–4 sentences).
            Respond in ${language}.
        """.trimIndent()

        return try {
            val response: Response = client.post(endpoint) {
                header("x-goog-api-key", apiKey)
                contentType(ContentType.Application.Json)
                setBody(Request(
                    contents = listOf(Content(role = "user", parts = listOf(Part(prompt)))),
                    generationConfig = GenConfig(temperature = 0.7, topP = 0.8, maxOutputTokens = 200)
                ))
            }.body()
            response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text?.trim()
                ?.ifBlank { null }
                ?: "Hi $userName, it's ${member.name}. We are all thinking of you today and love you very much."
        } catch (_: Throwable) {
            "Hi $userName, it's ${member.name}. We are all thinking of you today and love you very much."
        }
    }

    // ── expandMemory() ───────────────────────────────────────────────────────

    suspend fun expandMemory(
        hint: String,
        userName: String,
        language: AppLanguage
    ): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || hint.isBlank()) return hint

        val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"

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

        val response: Response = client.post(endpoint) {
            header("x-goog-api-key", apiKey)
            contentType(ContentType.Application.Json)
            setBody(Request(contents = listOf(Content(role = "user", parts = listOf(Part(prompt))))))
        }.body()

        val text = response.candidates
            .firstOrNull()?.content?.parts
            ?.firstOrNull()?.text
            ?.trim()
            .orEmpty()

        return text.ifBlank { hint }
    }
}
