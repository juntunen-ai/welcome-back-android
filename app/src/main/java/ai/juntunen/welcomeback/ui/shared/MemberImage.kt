package ai.juntunen.welcomeback.ui.shared

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ai.juntunen.welcomeback.services.PhotoStorage
import ai.juntunen.welcomeback.ui.theme.SurfaceVariant

/** Six gradient palettes — deterministic by name hash, mirrors iOS PersonAvatarPlaceholder. */
private val GRADIENTS = listOf(
    Pair(Color(0xFF4D9AFF), Color(0xFF2D6BCC)),
    Pair(Color(0xFF34C759), Color(0xFF1D8E3A)),
    Pair(Color(0xFFFFD600), Color(0xFFD4A800)),
    Pair(Color(0xFFFF6B6B), Color(0xFFCC3333)),
    Pair(Color(0xFFAF6BE6), Color(0xFF7B3DB5)),
    Pair(Color(0xFFFF9F40), Color(0xFFCC6D00)),
)

private fun gradientForName(name: String): Pair<Color, Color> {
    val idx = if (name.isBlank()) 0 else Math.abs(name.hashCode()) % GRADIENTS.size
    return GRADIENTS[idx]
}

private fun initials(name: String): String {
    val parts = name.trim().split(" ").filter { it.isNotBlank() }
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(1).uppercase()
        else            -> "${parts[0].take(1)}${parts.last().take(1)}".uppercase()
    }
}

/**
 * Circular avatar with initials + deterministic gradient.
 * Used when a person has no photo, mirroring iOS `PersonAvatarPlaceholder`.
 */
@Composable
fun PersonAvatarPlaceholder(
    name: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val (top, bottom) = gradientForName(name)
    val fontSize = (size.value * 0.38f).sp
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Brush.verticalGradient(listOf(top, bottom))),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials(name),
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Loads a member photo from internal storage (via `PhotoStorage.fileFor()`),
 * falling back to `PersonAvatarPlaceholder` when unavailable.
 */
@Composable
fun MemberImage(
    imageURL: String,
    name: String,
    size: Dp,
    isCircle: Boolean = true,
    cornerRadius: Dp = size / 4,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val file = if (imageURL.isNotBlank()) PhotoStorage.fileFor(context, imageURL) else null

    val shape = if (isCircle) CircleShape else RoundedCornerShape(cornerRadius)

    if (file != null) {
        AsyncImage(
            model = file,
            contentDescription = name,
            contentScale = contentScale,
            modifier = modifier.size(size).clip(shape).background(SurfaceVariant)
        )
    } else {
        if (isCircle) {
            PersonAvatarPlaceholder(name = name, size = size, modifier = modifier)
        } else {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(shape)
                    .background(SurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                val fontSize = (size.value * 0.28f).sp
                val (top, bottom) = gradientForName(name)
                Box(
                    modifier = Modifier.matchParentSize()
                        .background(Brush.verticalGradient(listOf(top, bottom)))
                )
                Text(
                    text = initials(name),
                    color = Color.White,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
