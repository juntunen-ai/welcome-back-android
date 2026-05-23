package ai.juntunen.welcomeback.ui.memories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.Memory
import ai.juntunen.welcomeback.data.MemoryCategory
import ai.juntunen.welcomeback.services.PhotoStorage
import ai.juntunen.welcomeback.services.TtsService
import ai.juntunen.welcomeback.ui.theme.*
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun MemoryStoryDetailScreen(memory: Memory, onBack: () -> Unit) {
    val lang    = LanguageManager
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()

    val ttsService = remember { TtsService(context) }
    val isPlaying by ttsService.isPlaying.collectAsStateWithLifecycle()

    DisposableEffect(Unit) { onDispose { ttsService.release() } }

    val speechText = remember(memory) {
        buildString {
            append(memory.title)
            if (memory.description.isNotBlank()) { append(". "); append(memory.description) }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {

        Column(modifier = Modifier.fillMaxSize()) {
            // Hero image or colour band
            Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {
                val file = if (memory.imageURL.isNotBlank()) PhotoStorage.fileFor(context, memory.imageURL) else null
                if (file != null) {
                    AsyncImage(
                        model = file,
                        contentDescription = memory.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    val gradientColors = when (memory.category) {
                        MemoryCategory.FAMILY -> listOf(Color(0xFF1A237E), Color(0xFF283593))
                        MemoryCategory.EVENTS -> listOf(Color(0xFF1B5E20), Color(0xFF2E7D32))
                        MemoryCategory.PLACES -> listOf(Color(0xFF4A148C), Color(0xFF6A1B9A))
                        MemoryCategory.OTHER  -> listOf(Color(0xFF212121), Color(0xFF424242))
                    }
                    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(gradientColors)))
                }
                // Gradient scrim
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(Brush.verticalGradient(
                            colors = listOf(Color.Transparent, BackgroundDark),
                            startY = 140f
                        ))
                )
                // Back button
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.padding(8.dp).align(Alignment.TopStart)
                        .clip(CircleShape).background(Color.Black.copy(alpha = 0.35f))
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = lang.t("back"), tint = Color.White)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(Modifier.height(8.dp))
                CategoryBadge(memory.category)
                Spacer(Modifier.height(10.dp))

                Text(
                    text = memory.title,
                    color = OnSurface,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )

                if (memory.date.isNotBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = null,
                            tint = AccentYellow, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(text = memory.date, color = OnSurface.copy(alpha = 0.5f), fontSize = 13.sp)
                    }
                }

                if (memory.description.isNotBlank()) {
                    Spacer(Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceVariant.copy(alpha = 0.4f))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = memory.description,
                            color = OnSurface.copy(alpha = 0.8f),
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                Spacer(Modifier.height(80.dp))
            }
        }

        // Floating play button
        if (speechText.isNotBlank()) {
            Box(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                    .background(Brush.verticalGradient(
                        listOf(Color.Transparent, BackgroundDark.copy(alpha = 0.95f))
                    ))
                    .padding(bottom = 32.dp, top = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (isPlaying) ttsService.stop()
                        else scope.launch { ttsService.speak(speechText) }
                    },
                    modifier = Modifier.height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentYellow,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(
                        text = if (isPlaying) lang.t("family.profile.stop") else lang.t("memories.story.hear"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryBadge(category: MemoryCategory) {
    val lang = LanguageManager
    val (label, color) = when (category) {
        MemoryCategory.FAMILY -> lang.t("memory.category.family") to Color(0xFF4FC3F7)
        MemoryCategory.EVENTS -> lang.t("memory.category.events") to Color(0xFFA5D6A7)
        MemoryCategory.PLACES -> lang.t("memory.category.places") to AccentYellow
        MemoryCategory.OTHER  -> lang.t("memory.category.other")  to OnSurface.copy(alpha = 0.5f)
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}
