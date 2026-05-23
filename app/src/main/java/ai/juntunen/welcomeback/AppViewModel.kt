package ai.juntunen.welcomeback

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ai.juntunen.welcomeback.data.*
import ai.juntunen.welcomeback.data.UserProfileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Single source of truth for the user's profile and app navigation state.
 * Mirrors the iOS `AppViewModel` — every screen reads from / writes to this VM.
 */
class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = UserProfileRepository(application)

    private val _userProfile = MutableStateFlow(UserProfile.DEFAULT)
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    /** Active tab in the main app. */
    private val _selectedTab = MutableStateFlow(AppTab.HOME)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    /** Whether the full-screen voice listening sheet is visible. */
    private val _listeningSheetPresented = MutableStateFlow(false)
    val listeningSheetPresented: StateFlow<Boolean> = _listeningSheetPresented.asStateFlow()

    init {
        viewModelScope.launch {
            _userProfile.value = repo.load()
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // Navigation
    // ─────────────────────────────────────────────────────────────────

    fun selectTab(tab: AppTab) { _selectedTab.value = tab }

    fun startListening() { _listeningSheetPresented.value = true }
    fun stopListening()  { _listeningSheetPresented.value = false }

    // ─────────────────────────────────────────────────────────────────
    // Profile mutations
    // ─────────────────────────────────────────────────────────────────

    fun updateName(name: String)                     = update { it.copy(name = name) }
    fun updateProfileImageURL(url: String)           = update { it.copy(profileImageURL = url) }
    fun updateAddress(address: String)               = update { it.copy(address = address) }
    fun updateBiography(bio: String)                 = update { it.copy(biography = bio) }
    fun updateCurrentLocation(loc: String)           = update { it.copy(currentLocation = loc) }
    fun updatePreferredVoiceMode(mode: VoiceMode)    = update { it.copy(preferredVoiceMode = mode) }
    fun updateNotificationsEnabled(enabled: Boolean) = update { it.copy(notificationsEnabled = enabled) }
    fun updateNotificationTimes(times: List<NotificationTime>) = update { it.copy(notificationTimes = times) }
    fun updateNotificationTopics(topics: String)     = update { it.copy(notificationTopics = topics) }

    // ─────────────────────────────────────────────────────────────────
    // Family members
    // ─────────────────────────────────────────────────────────────────

    fun addFamilyMember(member: FamilyMember) =
        update { it.copy(familyMembers = it.familyMembers + member) }

    fun updateFamilyMember(member: FamilyMember) =
        update { it.copy(familyMembers = it.familyMembers.map { m -> if (m.id == member.id) member else m }) }

    fun deleteFamilyMember(id: String) =
        update { it.copy(familyMembers = it.familyMembers.filter { m -> m.id != id }) }

    // ─────────────────────────────────────────────────────────────────
    // Memories
    // ─────────────────────────────────────────────────────────────────

    fun addMemory(memory: Memory) =
        update { it.copy(memories = it.memories + memory) }

    fun updateMemory(memory: Memory) =
        update { it.copy(memories = it.memories.map { m -> if (m.id == memory.id) memory else m }) }

    fun deleteMemory(id: String) =
        update { it.copy(memories = it.memories.filter { m -> m.id != id }) }

    // ─────────────────────────────────────────────────────────────────
    // Places
    // ─────────────────────────────────────────────────────────────────

    fun addPlace(place: Place) =
        update { it.copy(places = it.places + place) }

    fun updatePlace(place: Place) =
        update { it.copy(places = it.places.map { p -> if (p.id == place.id) place else p }) }

    fun deletePlace(id: String) =
        update { it.copy(places = it.places.filter { p -> p.id != id }) }

    // ─────────────────────────────────────────────────────────────────
    // Lifecycle
    // ─────────────────────────────────────────────────────────────────

    fun completeOnboarding() = update { it.copy(isOnboardingComplete = true) }

    fun resetToNewUser() {
        viewModelScope.launch {
            repo.clear()
            _userProfile.value = UserProfile.DEFAULT
            _selectedTab.value = AppTab.HOME
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // Internal
    // ─────────────────────────────────────────────────────────────────

    private fun update(transform: (UserProfile) -> UserProfile) {
        val next = _userProfile.updateAndGet(transform)
        viewModelScope.launch { repo.save(next) }
    }
}
