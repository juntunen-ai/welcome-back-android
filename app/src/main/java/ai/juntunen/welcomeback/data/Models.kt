package ai.juntunen.welcomeback.data

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Domain models — direct port of iOS `Models.swift` family.
 * Serialised with kotlinx.serialization so they can be persisted to DataStore
 * or sent to/from Gemini as JSON.
 */

@Serializable
data class FamilyMember(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val relationship: String,
    val phone: String = "",
    val biography: String = "",
    val memory1: String = "",
    val memory2: String = "",
    val imageURL: String = "",
    val additionalPhotoURLs: List<String> = emptyList(),
    val isVoiceCloned: Boolean = false,
    val voiceProfileID: String? = null
)

@Serializable
data class Memory(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val date: String = "",
    val imageURL: String = "",
    val category: MemoryCategory = MemoryCategory.OTHER,
    val description: String = ""
)

@Serializable
enum class MemoryCategory { FAMILY, EVENTS, PLACES, OTHER }

@Serializable
data class Place(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val imageURL: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

@Serializable
enum class VoiceMode { CLOUD, LOCAL }

@Serializable
enum class AIModel(val displayName: String, val apiModel: String) {
    GEMINI_FLASH("Gemini 2.0 Flash", "gemini-2.0-flash"),
    GEMINI_PRO("Gemini 2.5 Pro", "gemini-2.5-pro")
}

@Serializable
enum class NotificationTime(val hour: Int, val minute: Int) {
    MORNING(9, 0),
    NOON(12, 0),
    AFTERNOON(15, 0),
    EVENING(18, 0);

    companion object {
        val allCases = listOf(MORNING, NOON, AFTERNOON, EVENING)
    }
}

@Serializable
data class UserProfile(
    val name: String = "",
    val profileImageURL: String = "",
    val address: String = "",
    val biography: String = "",
    val currentLocation: String = "",
    val familyMembers: List<FamilyMember> = emptyList(),
    val memories: List<Memory> = emptyList(),
    val places: List<Place> = emptyList(),
    val preferredVoiceMode: VoiceMode = VoiceMode.LOCAL,
    val preferredAIModel: AIModel = AIModel.GEMINI_FLASH,
    val isVoiceCloningEnabled: Boolean = false,
    val notificationsEnabled: Boolean = false,
    val notificationTopics: String = "",
    val notificationTimes: List<NotificationTime> = listOf(NotificationTime.MORNING),
    val isOnboardingComplete: Boolean = false
) {
    companion object {
        val DEFAULT = UserProfile()
    }
}

/** Tab destinations for the main app navigation. */
enum class AppTab { HOME, MEMORIES, FAMILY, MUSIC, SETTINGS }
