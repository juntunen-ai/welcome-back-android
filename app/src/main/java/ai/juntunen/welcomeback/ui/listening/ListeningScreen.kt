package ai.juntunen.welcomeback.ui.listening

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.services.LiveSessionState
import ai.juntunen.welcomeback.ui.theme.*

/**
 * Full-screen voice conversation overlay.
 * Tries Gemini Live (WebSocket) first; falls back to text-based VoiceSession if it fails.
 */
@Composable
fun ListeningScreen(onDismiss: () -> Unit) {
    val lang = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsStateWithLifecycle()

    val liveVM: LiveSessionViewModel = viewModel()
    val voiceVM: VoiceSessionViewModel = viewModel()

    val liveState by liveVM.state.collectAsStateWithLifecycle()
    val useFallback by liveVM.useFallback.collectAsStateWithLifecycle()

    // Start session on first composition
    LaunchedEffect(Unit) {
        liveVM.beginSession(profile)
    }

    // If live session errors, also start fallback voice session
    LaunchedEffect(useFallback) {
        if (useFallback) voiceVM.startListening()
    }

    // Cleanup on dismiss
    DisposableEffect(Unit) {
        onDispose {
            liveVM.endSession()
            voiceVM.reset()
        }
    }

    if (useFallback) {
        FallbackVoiceUI(voiceVM = voiceVM, onDismiss = onDismiss)
    } else {
        LiveSessionUI(liveState = liveState, onDismiss = onDismiss) { liveVM.endSession(); onDismiss() }
    }
}

// ── Live session UI ───────────────────────────────────────────────────────────

@Composable
private fun LiveSessionUI(
    liveState: LiveSessionState,
    onDismiss: () -> Unit,
    onEnd: () -> Unit
) {
    val lang = LanguageManager

    // Determine blob color and glow intensity based on state
    val isAiSpeaking = liveState == LiveSessionState.AiSpeaking
    val isUserSpeaking = liveState == LiveSessionState.UserSpeaking
    val isListening = liveState == LiveSessionState.Listening

    val blobColor = when {
        isAiSpeaking -> AccentYellow
        isListening || isUserSpeaking -> AccentYellow.copy(alpha = 0.65f)
        else -> OnSurface.copy(alpha = 0.25f)
    }
    val glowAlpha = when {
        isAiSpeaking -> 0.18f
        isListening || isUserSpeaking -> 0.10f
        else -> 0.03f
    }

    // Pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "blob")
    val blobScale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "blob-scale"
    )
    val shouldPulse = isAiSpeaking || isListening || isUserSpeaking

    // Waveform bar animations (5 bars, staggered)
    val barHeights = listOf(16f, 24f, 40f, 24f, 16f)
    val barAnimations = barHeights.mapIndexed { idx, baseH ->
        infiniteTransition.animateFloat(
            initialValue = baseH,
            targetValue = baseH * 1.8f,
            animationSpec = infiniteRepeatable(
                tween(400 + idx * 80, easing = FastOutSlowInEasing),
                RepeatMode.Reverse
            ),
            label = "bar-$idx"
        )
    }
    val barVisible = isAiSpeaking || isListening || isUserSpeaking

    val statusText = when (liveState) {
        LiveSessionState.Connecting   -> lang.t("listening.connecting")
        LiveSessionState.Listening    -> lang.t("listening.ready")
        LiveSessionState.UserSpeaking -> lang.t("listening.go_on")
        LiveSessionState.AiThinking   -> lang.t("listening.moment")
        LiveSessionState.AiSpeaking   -> lang.t("listening.response")
        LiveSessionState.Interrupted  -> lang.t("listening.moment")
        LiveSessionState.Disconnected -> lang.t("listening.ended")
        is LiveSessionState.Error     -> liveState.message
        else -> ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        AccentYellow.copy(alpha = glowAlpha),
                        MaterialTheme.colorScheme.background
                    ),
                    radius = 900f
                )
            )
    ) {
        // Top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 52.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.TopCenter)
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            ) {
                Icon(Icons.Filled.Close, contentDescription = lang.t("common.cancel"), tint = OnSurface)
            }
            Text(
                text = "Welcome Back",
                color = OnSurface,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Centre content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Concentric ring borders + animated blob
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(240.dp)
            ) {
                // Outer ring
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .border(1.dp, blobColor.copy(alpha = 0.15f), CircleShape)
                )
                // Middle ring
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .border(1.dp, blobColor.copy(alpha = 0.25f), CircleShape)
                )
                // Blob
                Box(
                    modifier = Modifier
                        .scale(if (shouldPulse) blobScale else 1f)
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(blobColor.copy(alpha = 0.55f), blobColor.copy(alpha = 0.12f))
                            )
                        )
                )
            }

            Spacer(Modifier.height(24.dp))

            // Status text
            Text(
                text = statusText,
                color = OnSurface.copy(alpha = 0.75f),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }

        // Bottom controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Waveform bars
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(48.dp)
            ) {
                barAnimations.forEachIndexed { idx, animH ->
                    val h by animH
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(if (barVisible) h.dp else barHeights[idx].dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(blobColor.copy(alpha = if (barVisible) 0.75f else 0.2f))
                    )
                }
            }

            // End button
            Button(
                onClick = onEnd,
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = OnSurface
                ),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = lang.t("listening.end"),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
    }
}

// ── Fallback (text-based voice) UI ────────────────────────────────────────────

@Composable
private fun FallbackVoiceUI(
    voiceVM: VoiceSessionViewModel,
    onDismiss: () -> Unit
) {
    val lang = LanguageManager
    val phase by voiceVM.phase.collectAsStateWithLifecycle()
    val transcript by voiceVM.transcript.collectAsStateWithLifecycle()
    val reply by voiceVM.reply.collectAsStateWithLifecycle()
    val error by voiceVM.error.collectAsStateWithLifecycle()

    val infiniteTransition = rememberInfiniteTransition(label = "fallback-pulse")
    val blobScale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "fallback-scale"
    )
    val isListening = phase == VoiceSessionViewModel.Phase.LISTENING

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.97f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(64.dp))

            Text(
                text = lang.t("listening.title"),
                color = OnSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = when (phase) {
                    VoiceSessionViewModel.Phase.IDLE      -> lang.t("listening.hint")
                    VoiceSessionViewModel.Phase.LISTENING -> lang.t("listening.listening")
                    VoiceSessionViewModel.Phase.THINKING  -> lang.t("listening.thinking")
                    VoiceSessionViewModel.Phase.SPEAKING  -> lang.t("listening.speaking")
                    VoiceSessionViewModel.Phase.ERROR     -> lang.t("listening.error")
                },
                color = OnSurface.copy(alpha = 0.55f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(48.dp))

            // Blob
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(240.dp)
            ) {
                Box(modifier = Modifier.size(240.dp).border(1.dp, AccentYellow.copy(alpha = 0.15f), CircleShape))
                Box(modifier = Modifier.size(200.dp).border(1.dp, AccentYellow.copy(alpha = 0.25f), CircleShape))
                Box(
                    modifier = Modifier
                        .scale(if (isListening) blobScale else 1f)
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                if (isListening) listOf(AccentYellow.copy(0.55f), AccentYellow.copy(0.12f))
                                else listOf(OnSurface.copy(0.15f), OnSurface.copy(0.05f))
                            )
                        )
                )
            }

            Spacer(Modifier.height(28.dp))

            if (transcript.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(16.dp)
                ) {
                    Text("\"$transcript\"", color = OnSurface.copy(0.75f), fontSize = 16.sp)
                }
                Spacer(Modifier.height(12.dp))
            }

            if (reply.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(0.35f))
                        .padding(16.dp)
                ) {
                    Text(reply, color = OnSurface.copy(0.88f), fontSize = 16.sp, lineHeight = 24.sp)
                }
                Spacer(Modifier.height(12.dp))
            }

            if (error != null) {
                Text(error!!, color = Color(0xFFFF6B6B), fontSize = 13.sp, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
            }
        }

        // End button at bottom
        Button(
            onClick = onDismiss,
            shape = MaterialTheme.shapes.extraLarge,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = OnSurface
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 56.dp)
                .height(48.dp)
        ) {
            Text(lang.t("listening.end"), fontWeight = FontWeight.SemiBold, fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 24.dp))
        }

        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Icon(Icons.Filled.Close, contentDescription = lang.t("common.cancel"), tint = OnSurface)
        }
    }
}
