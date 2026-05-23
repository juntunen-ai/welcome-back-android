package ai.juntunen.welcomeback.ui.memories

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
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
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.Place
import ai.juntunen.welcomeback.services.PhotoStorage
import ai.juntunen.welcomeback.ui.theme.*
import coil.compose.AsyncImage

@Composable
fun PlaceDetailScreen(place: Place, onBack: () -> Unit) {
    val lang    = LanguageManager
    val context = LocalContext.current

    val hasCoords = place.latitude != 0.0 || place.longitude != 0.0

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {

        // Hero image
        Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
            val file = if (place.imageURL.isNotBlank()) PhotoStorage.fileFor(context, place.imageURL) else null
            if (file != null) {
                AsyncImage(
                    model = file,
                    contentDescription = place.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(Brush.verticalGradient(
                            listOf(Color(0xFF4A148C), Color(0xFF1A1A2E))
                        )),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Place, contentDescription = null,
                        tint = AccentYellow.copy(alpha = 0.4f),
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            // Gradient scrim
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(Brush.verticalGradient(
                        colors = listOf(Color.Transparent, BackgroundDark),
                        startY = 160f
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

            // Name
            Text(
                text = place.name,
                color = OnSurface,
                fontSize = 30.sp,
                fontWeight = FontWeight.Black
            )

            // Description
            if (place.description.isNotBlank()) {
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceVariant.copy(alpha = 0.4f))
                        .padding(16.dp)
                ) {
                    Text(
                        text = place.description,
                        color = OnSurface.copy(alpha = 0.78f),
                        fontSize = 15.sp,
                        lineHeight = 23.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            // Open in Maps button
            if (hasCoords) {
                Spacer(Modifier.height(20.dp))
                OutlinedButton(
                    onClick = {
                        val uri = Uri.parse("geo:${place.latitude},${place.longitude}?q=${Uri.encode(place.name)}")
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentYellow),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(AccentYellow.copy(alpha = 0.5f))
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Filled.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(lang.t("place.open.maps"), fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}
