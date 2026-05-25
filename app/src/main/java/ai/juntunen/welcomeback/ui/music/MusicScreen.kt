package ai.juntunen.welcomeback.ui.music

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.theme.*

@Composable
fun MusicScreen() {
    val lang = LanguageManager

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp, bottom = 16.dp)
        ) {
            Text(
                text = lang.t("music.title"),
                color = OnSurface,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(Modifier.weight(1f))

        // Centred illustration + copy
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Music icon circle
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(0xFF1DB954).copy(alpha = 0.22f),
                                Color(0xFF1DB954).copy(alpha = 0.05f)
                            )
                        )
                    )
                    .border(2.dp, Color(0xFF1DB954).copy(alpha = 0.35f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = null,
                    tint = Color(0xFF1DB954),
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = lang.t("music.subtitle"),
                color = OnSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = lang.t("music.body"),
                color = OnSurface.copy(alpha = 0.6f),
                fontSize = 15.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            // Spotify button (disabled — coming in v0.5)
            Button(
                onClick = {},
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color(0xFF1DB954).copy(alpha = 0.25f),
                    disabledContentColor = Color(0xFF1DB954).copy(alpha = 0.5f)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(lang.t("music.connect.spotify"), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = lang.t("music.coming.soon"),
                color = OnSurface.copy(alpha = 0.35f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.weight(1f))
    }
}
