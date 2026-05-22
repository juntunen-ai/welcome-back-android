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
    val isVoiceCloningEnabled: Boolean = false,
    val notificationsEnabled: Boolean = false,
    val notificationTopics: String = "",
    val isOnboardingComplete: Boolean = false
) {
    companion object {
        val DEFAULT = UserProfile()
    }
}
