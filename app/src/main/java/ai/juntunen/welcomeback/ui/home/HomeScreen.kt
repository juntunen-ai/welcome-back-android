package ai.juntunen.welcomeback.ui.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.FamilyMember
import ai.juntunen.welcomeback.services.LocationService
import ai.juntunen.welcomeback.ui.shared.MemberImage
import ai.juntunen.welcomeback.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onMicTap: () -> Unit,
    onMemberTap: (FamilyMember) -> Unit
) {
    val lang   = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()

    val locationService = remember { LocationService(context) }
    val city            by locationService.city.collectAsStateWithLifecycle()
    val street          by locationService.streetAddress.collectAsStateWithLifecycle()
    val coord           by locationService.coordinate.collectAsStateWithLifecycle()
    val locLoading      by locationService.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (locationService.hasPermission()) scope.launch { locationService.fetchOnce() }
    }

    val greeting = buildString {
        append(lang.t("home.greeting"))
        if (profile.name.isNotBlank()) append(", ${profile.name}")
        append("!")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(56.dp))

        // ── Greeting ────────────────────────────────────────────────
        Text(
            text = greeting,
            color = OnSurface,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = lang.t("home.intro.subtitle"),
            color = OnSurface.copy(alpha = 0.5f),
            fontSize = 15.sp
        )

        Spacer(Modifier.height(20.dp))

        // ── Location card ────────────────────────────────────────────
        LocationCard(
            city = city,
            street = street,
            isLoading = locLoading,
            onTap = {
                coord?.let { (lat, lon) ->
                    val q = city ?: "$lat,$lon"
                    val uri = Uri.parse("geo:$lat,$lon?q=${Uri.encode(q)}")
                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                }
            }
        )

        Spacer(Modifier.height(28.dp))

        // ── Mic button ───────────────────────────────────────────────
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .shadow(24.dp, CircleShape, spotColor = AccentYellow.copy(alpha = 0.4f))
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(AccentYellow.copy(alpha = 0.22f), AccentYellow.copy(alpha = 0.05f))
                        )
                    )
                    .border(2.dp, AccentYellow.copy(alpha = 0.35f), CircleShape)
                    .clickable { onMicTap() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Mic,
                    contentDescription = lang.t("home.mic.accessibility"),
                    tint = AccentYellow,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = lang.t("home.mic.label"),
            color = OnSurface.copy(alpha = 0.5f),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(28.dp))

        // ── Identity card ────────────────────────────────────────────
        if (profile.name.isNotBlank() || profile.biography.isNotBlank()) {
            IdentityCard(
                name = profile.name,
                biography = profile.biography,
                address = profile.address,
                imageURL = profile.profileImageURL
            )
            Spacer(Modifier.height(24.dp))
        }

        // ── Family row ───────────────────────────────────────────────
        if (profile.familyMembers.isNotEmpty()) {
            Text(
                text = lang.t("home.family.header"),
                color = OnSurface.copy(alpha = 0.5f),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp
            )
            Spacer(Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(profile.familyMembers) { member ->
                    FamilyCircle(member = member, onClick = { onMemberTap(member) })
                }
            }
            Spacer(Modifier.height(24.dp))
        }

        // ── About card ───────────────────────────────────────────────
        AboutCard()
        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun LocationCard(
    city: String?,
    street: String?,
    isLoading: Boolean,
    onTap: () -> Unit
) {
    val lang = LanguageManager
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .border(1.dp, Color.White.copy(alpha = 0.07f), MaterialTheme.shapes.large)
            .clickable(enabled = city != null) { onTap() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                tint = AccentYellow,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                when {
                    isLoading -> Text(
                        text = lang.t("home.location.finding"),
                        color = OnSurface.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                    city != null -> {
                        Text(text = city, color = OnSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        if (street != null) Text(
                            text = street,
                            color = OnSurface.copy(alpha = 0.55f),
                            fontSize = 13.sp
                        )
                    }
                    else -> Text(
                        text = lang.t("home.location.unavailable"),
                        color = OnSurface.copy(alpha = 0.4f),
                        fontSize = 14.sp
                    )
                }
            }
            if (city != null) {
                Text(text = "→", color = OnSurface.copy(alpha = 0.3f), fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun IdentityCard(name: String, biography: String, address: String, imageURL: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(1.dp, Color.White.copy(alpha = 0.07f), MaterialTheme.shapes.extraLarge)
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        MemberImage(
            imageURL = imageURL,
            name = name,
            size = 64.dp,
            isCircle = true,
            modifier = Modifier.border(2.dp, AccentYellow.copy(alpha = 0.6f), CircleShape)
        )
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            if (name.isNotBlank()) Text(
                text = name,
                color = OnSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            if (address.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(text = address, color = OnSurface.copy(alpha = 0.55f), fontSize = 13.sp)
            }
            if (biography.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = biography,
                    color = OnSurface.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun FamilyCircle(member: FamilyMember, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        MemberImage(
            imageURL = member.imageURL,
            name = member.name,
            size = 56.dp,
            isCircle = true
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = member.name.split(" ").firstOrNull() ?: member.name,
            color = OnSurface.copy(alpha = 0.75f),
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun AboutCard() {
    val lang = LanguageManager
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .border(1.dp, Color.White.copy(alpha = 0.06f), MaterialTheme.shapes.extraLarge)
            .padding(20.dp)
    ) {
        Text(
            text = lang.t("home.about.title"),
            color = AccentYellow,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = lang.t("home.about.body"),
            color = OnSurface.copy(alpha = 0.6f),
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
    }
}
