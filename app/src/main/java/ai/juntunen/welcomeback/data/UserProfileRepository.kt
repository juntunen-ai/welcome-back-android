package ai.juntunen.welcomeback.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.profileDataStore by preferencesDataStore(name = "welcomeback_profile")

private val PROFILE_KEY = stringPreferencesKey("profile_json")

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
}

/**
 * Persists [UserProfile] as a single JSON blob inside Preferences DataStore.
 *
 * We keep it as one JSON string (vs. individual preference keys) because the
 * profile graph is non-trivial — nested lists of family members, memories,
 * places — and round-tripping through one [kotlinx.serialization] call is
 * simpler and migration-friendly. The model classes use Codable-style
 * defaults so adding new fields stays backward-compatible.
 */
class UserProfileRepository(private val context: Context) {

    val profileFlow: Flow<UserProfile> = context.profileDataStore.data
        .map { prefs ->
            val raw = prefs[PROFILE_KEY] ?: return@map UserProfile.DEFAULT
            runCatching { json.decodeFromString<UserProfile>(raw) }
                .getOrDefault(UserProfile.DEFAULT)
        }

    suspend fun load(): UserProfile = profileFlow.first()

    suspend fun save(profile: UserProfile) {
        val encoded = json.encodeToString(profile)
        context.profileDataStore.edit { it[PROFILE_KEY] = encoded }
    }

    suspend fun clear() {
        context.profileDataStore.edit { it.remove(PROFILE_KEY) }
    }
}
