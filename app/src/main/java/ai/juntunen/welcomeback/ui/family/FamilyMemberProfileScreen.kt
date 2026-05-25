package ai.juntunen.welcomeback.ui.family

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.FamilyMember
import ai.juntunen.welcomeback.services.PhotoStorage
import ai.juntunen.welcomeback.services.TtsService
import ai.juntunen.welcomeback.ui.shared.MemberImage
import ai.juntunen.welcomeback.ui.theme.*
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun FamilyMemberProfileScreen(member: FamilyMember, onBack: () -> Unit) {
    val lang    = LanguageManager
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()

    // Build TTS text: biography + memories
    val speechText = buildString {
        if (member.biography.isNotBlank()) { append(member.biography); append(" ") }
        if (member.memory1.isNotBlank())   { append(member.memory1);   append(" ") }
        if (member.memory2.isNotBlank())   append(member.memory2)
    }.trim()

    val ttsService = remember { TtsService(context) }
    val isPlaying by ttsService.isPlaying.collectAsStateWithLifecycle()

    DisposableEffect(Unit) { onDispose { ttsService.release() } }

    // Build photo list: primary + additional
    val allPhotos = remember(member) {
        buildList {
            if (member.imageURL.isNotBlank()) add(member.imageURL)
            addAll(member.additionalPhotoURLs)
        }
    }
    val pagerState = rememberPagerState { allPhotos.size.coerceAtLeast(1) }

    // Play-button pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulse-scale"
    )

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {

            // ── Photo pager ──────────────────────────────────────────
            Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                if (allPhotos.isEmpty()) {
                    MemberImage(
                        imageURL = "",
                        name = member.name,
                        size = 320.dp,
                        isCircle = false,
                        cornerRadius = 0.dp,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val file = PhotoStorage.fileFor(context, allPhotos[page])
                        if (file != null) {
                            AsyncImage(
                                model = file,
                                contentDescription = member.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            MemberImage(
                                imageURL = allPhotos[page],
                                name = member.name,
                                size = 320.dp,
                                isCircle = false,
                                cornerRadius = 0.dp,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                // Gradient scrim at bottom
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(Brush.verticalGradient(
                            colors = listOf(Color.Transparent, BackgroundDark),
                            startY = 200f
                        ))
                )

                // Back button
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.padding(8.dp).align(Alignment.TopStart)
                        .clip(CircleShape).background(Color.Black.copy(alpha = 0.35f))
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = lang.t("back"),
                        tint = Color.White)
                }

                // Dot indicators
                if (allPhotos.size > 1) {
                    Row(
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        repeat(allPhotos.size) { i ->
                            Box(
                                modifier = Modifier
                                    .size(if (i == pagerState.currentPage) 20.dp else 6.dp, 6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (i == pagerState.currentPage) AccentYellow
                                        else Color.White.copy(alpha = 0.35f)
                                    )
                            )
                        }
                    }
                }
            }

            // ── Name & relationship ───────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = member.name,
                        color = OnSurface,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.weight(1f)
                    )
                    if (member.relationship.isNotBlank()) RelationshipBadge(member.relationship)
                }
                if (member.phone.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(text = member.phone, color = OnSurface.copy(alpha = 0.45f), fontSize = 14.sp)
                }

                // ── Biography ─────────────────────────────────────────
                if (member.biography.isNotBlank()) {
                    Spacer(Modifier.height(20.dp))
                    SectionCard(title = lang.t("family.profile.biography")) {
                        Text(
                            text = member.biography,
                            color = OnSurface.copy(alpha = 0.75f),
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                // ── Memories ──────────────────────────────────────────
                listOf(member.memory1, member.memory2).filter { it.isNotBlank() }
                    .forEachIndexed { idx, mem ->
                        Spacer(Modifier.height(12.dp))
                        SectionCard(
                            title = if (idx == 0) lang.t("family.profile.memory1")
                                    else lang.t("family.profile.memory2"),
                            icon = true
                        ) {
                            Text(
                                text = mem,
                                color = OnSurface.copy(alpha = 0.75f),
                                fontSize = 15.sp,
                                lineHeight = 22.sp
                            )
                        }
                    }

                Spacer(Modifier.height(100.dp))
            }
        }

        // ── Floating play button ─────────────────────────────────────
        if (speechText.isNotBlank()) {
            Box(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, BackgroundDark.copy(alpha = 0.96f)),
                            startY = 0f
                        )
                    )
                    .padding(bottom = 32.dp, top = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (isPlaying) ttsService.stop()
                        else scope.launch { ttsService.speak(speechText) }
                    },
                    modifier = Modifier.scale(if (isPlaying) 1f else pulseScale).height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentYellow,
                        contentColor   = Color.Black
                    ),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isPlaying) lang.t("family.profile.stop")
                               else lang.t("family.profile.hear", member.name.split(" ").first()),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, icon: Boolean = false, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(1.dp, Color.White.copy(alpha = 0.06f), MaterialTheme.shapes.large)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon) {
                Icon(Icons.Filled.Star, contentDescription = null,
                    tint = AccentYellow, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
            }
            Text(
                text = title.uppercase(),
                color = OnSurface.copy(alpha = 0.4f),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp
            )
        }
        Spacer(Modifier.height(10.dp))
        content()
    }
}
