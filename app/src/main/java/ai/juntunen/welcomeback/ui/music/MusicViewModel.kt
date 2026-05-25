package ai.juntunen.welcomeback.ui.music

import android.app.Application
import android.content.ContentUris
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class MusicAuthStatus { NOT_REQUESTED, GRANTED, DENIED }

data class MediaTrack(
    val id: Long,
    val title: String,
    val artist: String,
    val albumId: Long,
    val durationMs: Long
)

class MusicViewModel(app: Application) : AndroidViewModel(app) {

    private val _authStatus = MutableStateFlow(MusicAuthStatus.NOT_REQUESTED)
    val authStatus: StateFlow<MusicAuthStatus> = _authStatus.asStateFlow()

    private val _tracks = MutableStateFlow<List<MediaTrack>>(emptyList())
    val tracks: StateFlow<List<MediaTrack>> = _tracks.asStateFlow()

    private val _currentTrack = MutableStateFlow<MediaTrack?>(null)
    val currentTrack: StateFlow<MediaTrack?> = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _memoryLaneIsPlaying = MutableStateFlow(false)
    val memoryLaneIsPlaying: StateFlow<Boolean> = _memoryLaneIsPlaying.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private var memoryLaneQueue: MutableList<MediaTrack> = mutableListOf()
    private var memoryLaneIndex: Int = 0

    // ── Permission ────────────────────────────────────────────────────────────

    fun checkPermission() {
        val ctx = getApplication<Application>()
        val perm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_AUDIO
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        val granted = ctx.checkSelfPermission(perm) == android.content.pm.PackageManager.PERMISSION_GRANTED
        _authStatus.value = if (granted) MusicAuthStatus.GRANTED else MusicAuthStatus.NOT_REQUESTED
        if (granted) loadTracks()
    }

    fun onPermissionResult(granted: Boolean) {
        _authStatus.value = if (granted) MusicAuthStatus.GRANTED else MusicAuthStatus.DENIED
        if (granted) loadTracks()
    }

    // ── Loading ───────────────────────────────────────────────────────────────

    fun loadTracks() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = withContext(Dispatchers.IO) { queryTracks() }
            _tracks.value = result
            _isLoading.value = false
        }
    }

    private fun queryTracks(): List<MediaTrack> {
        val ctx = getApplication<Application>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
        )
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val sortOrder = "${MediaStore.Audio.Media.DATE_MODIFIED} DESC"

        val list = mutableListOf<MediaTrack>()
        ctx.contentResolver.query(uri, projection, null, null, sortOrder)?.use { cursor ->
            val idCol       = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol    = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol   = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumIdCol  = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext() && list.size < 50) {
                list.add(
                    MediaTrack(
                        id         = cursor.getLong(idCol),
                        title      = cursor.getString(titleCol) ?: "Unknown",
                        artist     = cursor.getString(artistCol) ?: "Unknown",
                        albumId    = cursor.getLong(albumIdCol),
                        durationMs = cursor.getLong(durationCol)
                    )
                )
            }
        }
        return list
    }

    // ── Playback ──────────────────────────────────────────────────────────────

    fun play(track: MediaTrack) {
        val ctx = getApplication<Application>()
        val trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, track.id)
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(ctx, trackUri)
                prepare()
                start()
                setOnCompletionListener {
                    _isPlaying.value = false
                    if (_memoryLaneIsPlaying.value) advanceMemoryLane()
                }
            }
            _currentTrack.value = track
            _isPlaying.value = true
        } catch (e: Exception) {
            _errorMessage.value = "Playback error: ${e.message}"
        }
    }

    fun togglePlayback() {
        val player = mediaPlayer ?: return
        if (player.isPlaying) {
            player.pause()
            _isPlaying.value = false
        } else {
            player.start()
            _isPlaying.value = true
        }
    }

    fun skipForward() {
        val queue = if (_memoryLaneIsPlaying.value) memoryLaneQueue else _tracks.value.toMutableList()
        val idx = queue.indexOfFirst { it.id == _currentTrack.value?.id }
        val next = queue.getOrNull(idx + 1)
        if (next != null) play(next)
    }

    fun skipBackward() {
        val queue = if (_memoryLaneIsPlaying.value) memoryLaneQueue else _tracks.value.toMutableList()
        val idx = queue.indexOfFirst { it.id == _currentTrack.value?.id }
        val prev = queue.getOrNull(idx - 1)
        if (prev != null) play(prev)
    }

    // ── Memory lane ───────────────────────────────────────────────────────────

    fun startMemoryLane() {
        val shuffled = _tracks.value.shuffled()
        if (shuffled.isEmpty()) return
        memoryLaneQueue = shuffled.toMutableList()
        memoryLaneIndex = 0
        _memoryLaneIsPlaying.value = true
        play(memoryLaneQueue[0])
    }

    fun stopMemoryLane() {
        _memoryLaneIsPlaying.value = false
        mediaPlayer?.stop()
        _isPlaying.value = false
    }

    private fun advanceMemoryLane() {
        memoryLaneIndex++
        val next = memoryLaneQueue.getOrNull(memoryLaneIndex)
        if (next != null) play(next) else _memoryLaneIsPlaying.value = false
    }

    // ── Cleanup ───────────────────────────────────────────────────────────────

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
