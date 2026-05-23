package ai.juntunen.welcomeback

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ai.juntunen.welcomeback.data.FamilyMember
import ai.juntunen.welcomeback.data.Memory
import ai.juntunen.welcomeback.data.Place
import ai.juntunen.welcomeback.data.UserProfile
import ai.juntunen.welcomeback.data.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Single source of truth for the user's profile. Mirrors the iOS
 * `AppViewModel` — every screen reads from / writes to this VM, and changes
 * are persisted to DataStore in the background.
 */
class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = UserProfileRepository(application)

    private val _userProfile = MutableStateFlow(UserProfile.DEFAULT)
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    init {
        // Hydrate from disk on launch
        viewModelScope.launch {
            _userProfile.value = repo.load()
        }
    }

    // MARK: - Mutations

    fun updateName(name: String) = update { it.copy(name = name) }

    fun updateProfileImageURL(url: String) = update { it.copy(profileImageURL = url) }

    fun addFamilyMember(member: FamilyMember) =
        update { it.copy(familyMembers = it.familyMembers + member) }

    fun addPlace(place: Place) =
        update { it.copy(places = it.places + place) }

    fun addMemory(memory: Memory) =
        update { it.copy(memories = it.memories + memory) }

    fun completeOnboarding() = update { it.copy(isOnboardingComplete = true) }

    fun resetToNewUser() {
        viewModelScope.launch {
            repo.clear()
            _userProfile.value = UserProfile.DEFAULT
        }
    }

    /** Apply [transform] to the current profile, publish, and persist. */
    private fun update(transform: (UserProfile) -> UserProfile) {
        _userProfile.update(transform)
        viewModelScope.launch { repo.save(_userProfile.value) }
    }
}
