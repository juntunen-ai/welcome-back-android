package ai.juntunen.welcomeback.ui.music

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.theme.*

@Composable
fun MusicScreen() {
    val lang = LanguageManager
    val musicVM: MusicViewModel = viewModel()
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsStateWithLifecycle()

    val authStatus by musicVM.authStatus.collectAsStateWithLifecycle()
    val tracks by musicVM.tracks.collectAsStateWithLifecycle()
    val currentTrack by musicVM.currentTrack.collectAsStateWithLifecycle()
    val isPlaying by musicVM.isPlaying.collectAsStateWithLifecycle()
    val isLoading by musicVM.isLoading.collectAsStateWithLifecycle()
    val memoryLaneIsPlaying by musicVM.memoryLaneIsPlaying.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        musicVM.onPermissionResult(granted)
    }

    // Check permission on first composition
    LaunchedEffect(Unit) { musicVM.checkPermission() }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = if (currentTrack != null) 80.dp else 16.dp)
        ) {
            // Header
            item {
                Text(
                    text = lang.t("music.title"),
                    color = OnSurface,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 56.dp, bottom = 16.dp)
                )
            }

            // Memory Lane card
            item {
                MemoryLaneCard(
                    userName = profile.name.ifBlank { lang.t("home.greeting") },
                    isPlaying = memoryLaneIsPlaying,
                    hasTrack = currentTrack != null && memoryLaneIsPlaying,
                    onPlay = { if (memoryLaneIsPlaying) musicVM.stopMemoryLane() else musicVM.startMemoryLane() },
                    onSkipBack = { musicVM.skipBackward() },
                    onSkipForward = { musicVM.skipForward() }
                )
                Spacer(Modifier.height(24.dp))
            }

            // Library section header
            item {
                Text(
                    text = lang.t("music.library.header"),
                    color = OnSurface.copy(alpha = 0.4f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                )
            }

            // Library content
            item {
                when (authStatus) {
                    MusicAuthStatus.NOT_REQUESTED -> {
                        ConnectMusicCard {
                            val perm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                android.Manifest.permission.READ_MEDIA_AUDIO
                            else android.Manifest.permission.READ_EXTERNAL_STORAGE
                            permissionLauncher.launch(perm)
                        }
                    }
                    MusicAuthStatus.DENIED -> {
                        DeniedMusicCard()
                    }
                    MusicAuthStatus.GRANTED -> {
                        if (isLoading) {
                            Box(Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = AccentYellow)
                            }
                        } else if (tracks.isEmpty()) {
                            Box(Modifier.fillMaxWidth().padding(24.dp)) {
                                Text(lang.t("music.library.empty"), color = OnSurface.copy(0.5f), fontSize = 14.sp)
                            }
                        } else {
                            TrackGrid(
                                tracks = tracks,
                                currentTrack = currentTrack,
                                onTrackTap = { musicVM.play(it) }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            // Memory Mixes section header
            item {
                Text(
                    text = lang.t("music.mixes.header"),
                    color = OnSurface.copy(alpha = 0.4f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                )
            }

            // Memory Mixes placeholder grid
            item {
                MemoryMixesGrid()
                Spacer(Modifier.height(24.dp))
            }
        }

        // Now Playing bar
        if (currentTrack != null) {
            NowPlayingBar(
                track = currentTrack!!,
                isPlaying = isPlaying,
                onToggle = { musicVM.togglePlayback() },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

// ── Memory Lane Card ─────────────────────────────────────────────────────────

@Composable
private fun MemoryLaneCard(
    userName: String,
    isPlaying: Boolean,
    hasTrack: Boolean,
    onPlay: () -> Unit,
    onSkipBack: () -> Unit,
    onSkipForward: () -> Unit
) {
    val lang = LanguageManager

    val infiniteTransition = rememberInfiniteTransition(label = "lane-glow")
    val borderAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "border-alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(
                2.dp,
                AccentYellow.copy(alpha = if (isPlaying) borderAlpha else 0.3f),
                MaterialTheme.shapes.extraLarge
            )
            .padding(20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            // Icon circle
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(listOf(AccentYellow.copy(0.25f), AccentYellow.copy(0.05f)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.MusicNote,
                    contentDescription = null,
                    tint = AccentYellow,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = userName,
                color = OnSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = lang.t("music.memorylane.subtitle"),
                color = AccentYellow,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(16.dp))

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onSkipBack) {
                    Icon(Icons.Filled.SkipPrevious, contentDescription = null, tint = OnSurface.copy(0.7f), modifier = Modifier.size(32.dp))
                }
                // Play/stop button
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(AccentYellow)
                        .clickable { onPlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (isPlaying) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }
                IconButton(onClick = onSkipForward) {
                    Icon(Icons.Filled.SkipNext, contentDescription = null, tint = OnSurface.copy(0.7f), modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

// ── Connect + Denied cards ────────────────────────────────────────────────────

@Composable
private fun ConnectMusicCard(onConnect: () -> Unit) {
    val lang = LanguageManager
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Filled.LibraryMusic, contentDescription = null,
                tint = AccentYellow, modifier = Modifier.size(36.dp))
            Spacer(Modifier.height(12.dp))
            Text(lang.t("music.connect.title"), color = OnSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(lang.t("music.connect.subtitle"), color = OnSurface.copy(0.55f), fontSize = 13.sp)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onConnect,
                colors = ButtonDefaults.buttonColors(containerColor = AccentYellow, contentColor = Color.Black),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(lang.t("music.connect.button"), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun DeniedMusicCard() {
    val lang = LanguageManager
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(20.dp)
    ) {
        Column {
            Text(lang.t("music.denied.title"), color = OnSurface, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(lang.t("music.denied.body"), color = OnSurface.copy(0.55f), fontSize = 13.sp, lineHeight = 19.sp)
        }
    }
}

// ── Track grid ────────────────────────────────────────────────────────────────

@Composable
private fun TrackGrid(
    tracks: List<MediaTrack>,
    currentTrack: MediaTrack?,
    onTrackTap: (MediaTrack) -> Unit
) {
    val columns = 3
    val chunked = tracks.chunked(columns)
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        chunked.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                row.forEach { track ->
                    TrackCard(
                        track = track,
                        isActive = track.id == currentTrack?.id,
                        onTap = { onTrackTap(track) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill empty cells
                repeat(columns - row.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TrackCard(
    track: MediaTrack,
    isActive: Boolean,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val albumArtUri = ContentUris.withAppendedId(
        Uri.parse("content://media/external/audio/albumart"), track.albumId
    )
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .border(
                if (isActive) 2.dp else 0.dp,
                if (isActive) AccentYellow else Color.Transparent,
                MaterialTheme.shapes.medium
            )
            .clickable { onTap() }
    ) {
        AsyncImage(
            model = albumArtUri,
            contentDescription = track.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Title overlay at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(0.55f))
                .padding(4.dp)
        ) {
            Column {
                Text(
                    track.title,
                    color = OnSurface,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    track.artist,
                    color = OnSurface.copy(0.6f),
                    fontSize = 8.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ── Memory Mixes ──────────────────────────────────────────────────────────────

@Composable
private fun MemoryMixesGrid() {
    val lang = LanguageManager
    val mixes = listOf(
        lang.t("music.mix.1"), lang.t("music.mix.2"),
        lang.t("music.mix.3"), lang.t("music.mix.4")
    )
    val colors = listOf(
        Color(0xFF7B68EE), Color(0xFF20B2AA),
        Color(0xFFCD853F), Color(0xFF4682B4)
    )
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        mixes.chunked(2).forEachIndexed { rowIdx, row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                row.forEachIndexed { colIdx, name ->
                    val colorIdx = rowIdx * 2 + colIdx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .clip(MaterialTheme.shapes.large)
                            .background(
                                Brush.linearGradient(listOf(colors[colorIdx].copy(0.4f), colors[colorIdx].copy(0.15f)))
                            )
                            .border(1.dp, colors[colorIdx].copy(0.4f), MaterialTheme.shapes.large),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(name, color = OnSurface, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

// ── Now Playing bar ───────────────────────────────────────────────────────────

@Composable
private fun NowPlayingBar(
    track: MediaTrack,
    isPlaying: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val albumArtUri = ContentUris.withAppendedId(
        Uri.parse("content://media/external/audio/albumart"), track.albumId
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Album art
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            ) {
                AsyncImage(
                    model = albumArtUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    track.title,
                    color = OnSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    track.artist,
                    color = OnSurface.copy(0.55f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Play/pause button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AccentYellow)
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
