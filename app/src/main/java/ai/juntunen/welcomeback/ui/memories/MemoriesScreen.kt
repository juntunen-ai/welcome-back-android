package ai.juntunen.welcomeback.ui.memories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.FamilyMember
import ai.juntunen.welcomeback.data.Memory
import ai.juntunen.welcomeback.data.MemoryCategory
import ai.juntunen.welcomeback.data.Place
import ai.juntunen.welcomeback.services.PhotoStorage
import ai.juntunen.welcomeback.ui.shared.MemberImage
import ai.juntunen.welcomeback.ui.theme.*
import coil.compose.AsyncImage

@Composable
fun MemoriesScreen(
    onMemberTap: (FamilyMember) -> Unit,
    onPlaceTap: (Place) -> Unit,
    onMemoryTap: (Memory) -> Unit
) {
    val lang  = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 56.dp, bottom = 16.dp)
            ) {
                Text(
                    text = lang.t("memories.title"),
                    color = OnSurface,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // ── Family section ───────────────────────────────────────────
        if (profile.familyMembers.isNotEmpty()) {
            item {
                SectionHeader(title = lang.t("memories.family.header"))
            }
            items(profile.familyMembers) { member ->
                FamilyMemoryCard(member = member, onClick = { onMemberTap(member) })
                Spacer(Modifier.height(12.dp))
            }
            item { Spacer(Modifier.height(8.dp)) }
        }

        // ── Places section ───────────────────────────────────────────
        if (profile.places.isNotEmpty()) {
            item {
                SectionHeader(title = lang.t("memories.places.header"))
            }
            items(profile.places) { place ->
                PlaceCard(place = place, onClick = { onPlaceTap(place) })
                Spacer(Modifier.height(12.dp))
            }
            item { Spacer(Modifier.height(8.dp)) }
        }

        // ── Memories section ─────────────────────────────────────────
        if (profile.memories.isNotEmpty()) {
            item {
                SectionHeader(title = lang.t("memories.stories.header"))
            }
            items(profile.memories) { memory ->
                MemoryCard(memory = memory, onClick = { onMemoryTap(memory) })
                Spacer(Modifier.height(12.dp))
            }
        }

        // Empty state
        if (profile.familyMembers.isEmpty() && profile.places.isEmpty() && profile.memories.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillParentMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PhotoLibrary,
                            contentDescription = null,
                            tint = OnSurface.copy(alpha = 0.2f),
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = lang.t("memories.empty"),
                            color = OnSurface.copy(alpha = 0.4f),
                            fontSize = 16.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        color = OnSurface.copy(alpha = 0.4f),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
    )
}

@Composable
private fun FamilyMemoryCard(member: FamilyMember, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MemberImage(imageURL = member.imageURL, name = member.name, size = 56.dp, isCircle = true)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(text = member.name, color = OnSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            if (member.relationship.isNotBlank())
                Text(text = member.relationship, color = OnSurface.copy(alpha = 0.5f), fontSize = 13.sp)
            if (member.biography.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = member.biography,
                    color = OnSurface.copy(alpha = 0.45f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = OnSurface.copy(alpha = 0.25f),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun PlaceCard(place: Place, onClick: () -> Unit) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable { onClick() }
            .height(140.dp)
    ) {
        if (place.imageURL.isNotBlank()) {
            val file = PhotoStorage.fileFor(context, place.imageURL)
            if (file != null) {
                AsyncImage(
                    model = file,
                    contentDescription = place.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxSize()
                .background(Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                    startY = 40f
                ))
        )
        Column(
            modifier = Modifier.align(Alignment.BottomStart).padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Place, contentDescription = null,
                    tint = AccentYellow, modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(text = place.name, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
            if (place.description.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = place.description,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun MemoryCard(memory: Memory, onClick: () -> Unit) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable { onClick() }
    ) {
        Column {
            // Image
            if (memory.imageURL.isNotBlank()) {
                val file = PhotoStorage.fileFor(context, memory.imageURL)
                if (file != null) {
                    AsyncImage(
                        model = file,
                        contentDescription = memory.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(160.dp)
                    )
                }
            }
            // Content
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CategoryChip(memory.category)
                    Spacer(Modifier.weight(1f))
                    if (memory.date.isNotBlank()) {
                        Text(text = memory.date, color = OnSurface.copy(alpha = 0.4f), fontSize = 11.sp)
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = memory.title,
                    color = OnSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (memory.description.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = memory.description,
                        color = OnSurface.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(category: MemoryCategory) {
    val lang = LanguageManager
    val (label, color) = when (category) {
        MemoryCategory.FAMILY -> lang.t("memory.category.family") to Color(0xFF4FC3F7)
        MemoryCategory.EVENTS -> lang.t("memory.category.events") to Color(0xFFA5D6A7)
        MemoryCategory.PLACES -> lang.t("memory.category.places") to AccentYellow
        MemoryCategory.OTHER  -> lang.t("memory.category.other")  to OnSurface.copy(alpha = 0.5f)
    }
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text = label, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
