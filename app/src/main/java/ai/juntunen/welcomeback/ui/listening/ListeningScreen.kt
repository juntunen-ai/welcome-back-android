package ai.juntunen.welcomeback.ui.listening

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.theme.*

/**
 * Full-screen voice conversation overlay. Presented as a conditional in
 * [ai.juntunen.welcomeback.ui.MainAppScreen] when the mic button is tapped.
 */
@Composable
fun ListeningScreen(onDismiss: () -> Unit) {
    val lang   = LanguageManager
    val vm: VoiceSessionViewModel = viewModel()

    val phase      by vm.phase.collectAsStateWithLifecycle()
    val transcript by vm.transcript.collectAsStateWithLifecycle()
    val reply      by vm.reply.collectAsStateWithLifecycle()
    val error      by vm.error.collectAsStateWithLifecycle()

    // Pulse animation for the mic ring
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.12f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "pulse-scale"
    )
    val isListening = phase == VoiceSessionViewModel.Phase.LISTENING

    // Auto-start listening when screen opens
    LaunchedEffect(Unit) { vm.startListening() }

    // Cleanup when dismissed
    DisposableEffect(Unit) {
        onDispose { vm.reset() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.97f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(64.dp))

            // Title
            Text(
                text = lang.t("listening.title"),
                color = OnSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(6.dp))

            // Phase subtitle
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

            // ── Mic button ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .scale(if (isListening) pulseScale else 1f)
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            when {
                                isListening -> listOf(
                                    AccentYellow.copy(alpha = 0.28f),
                                    AccentYellow.copy(alpha = 0.07f)
                                )
                                phase == VoiceSessionViewModel.Phase.THINKING ||
                                phase == VoiceSessionViewModel.Phase.SPEAKING -> listOf(
                                    Color(0xFF4FC3F7).copy(alpha = 0.28f),
                                    Color(0xFF4FC3F7).copy(alpha = 0.07f)
                                )
                                else -> listOf(
                                    OnSurface.copy(alpha = 0.10f),
                                    OnSurface.copy(alpha = 0.03f)
                                )
                            }
                        )
                    )
                    .border(
                        2.dp,
                        if (isListening) AccentYellow.copy(alpha = 0.45f)
                        else OnSurface.copy(alpha = 0.12f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (phase) {
                        VoiceSessionViewModel.Phase.LISTENING -> Icons.Filled.Mic
                        else                                  -> Icons.Filled.Mic
                    },
                    contentDescription = null,
                    tint = if (isListening) AccentYellow else OnSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(62.dp)
                )
            }

            Spacer(Modifier.height(40.dp))

            // ── Live transcript ─────────────────────────────────────
            if (transcript.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "\"$transcript\"",
                        color = OnSurface.copy(alpha = 0.75f),
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 24.sp
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            // ── AI reply ────────────────────────────────────────────
            if (reply.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f))
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), MaterialTheme.shapes.large)
                        .padding(16.dp)
                ) {
                    Text(
                        text = reply,
                        color = OnSurface.copy(alpha = 0.88f),
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            // ── Error ────────────────────────────────────────────────
            if (error != null) {
                Text(
                    text = error!!,
                    color = Color(0xFFFF6B6B),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(16.dp))

            // ── Action buttons ──────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Stop / Start listening
                Button(
                    onClick = {
                        if (isListening) vm.stopListening() else vm.startListening()
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isListening) Color(0xFFFF6B6B) else AccentYellow,
                        contentColor = Color.Black
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        if (isListening) Icons.Filled.Stop else Icons.Filled.Mic,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = if (isListening) lang.t("listening.stop") else lang.t("listening.start"),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
        }

        // Dismiss (X) button top-right
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Icon(Icons.Filled.Close, contentDescription = lang.t("common.cancel"), tint = OnSurface)
        }
    }
}
