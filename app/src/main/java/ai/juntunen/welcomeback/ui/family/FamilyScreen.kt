package ai.juntunen.welcomeback.ui.family

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
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
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.FamilyMember
import ai.juntunen.welcomeback.ui.shared.MemberImage
import ai.juntunen.welcomeback.ui.theme.*

@Composable
fun FamilyScreen(onMemberTap: (FamilyMember) -> Unit) {
    val lang  = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsStateWithLifecycle()
    val members = profile.familyMembers

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
                text = lang.t("family.title"),
                color = OnSurface,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )
        }

        if (members.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.People,
                        contentDescription = null,
                        tint = OnSurface.copy(alpha = 0.2f),
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = lang.t("family.empty"),
                        color = OnSurface.copy(alpha = 0.4f),
                        fontSize = 16.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(members) { member ->
                    FamilyAlbumCard(member = member, onClick = { onMemberTap(member) })
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
fun FamilyAlbumCard(member: FamilyMember, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable { onClick() }
    ) {
        // Hero image
        MemberImage(
            imageURL = member.imageURL,
            name = member.name,
            size = 200.dp,
            isCircle = false,
            cornerRadius = 0.dp,
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentScale = ContentScale.Crop
        )

        // Gradient scrim over bottom half
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 80f
                    )
                )
        )

        // Text overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = member.name,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                RelationshipBadge(member.relationship)
            }
            if (member.biography.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = member.biography,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun RelationshipBadge(relationship: String) {
    if (relationship.isBlank()) return
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(AccentYellow.copy(alpha = 0.9f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = relationship,
            color = Color.Black,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/** Compact row used in FamilyManagement and PersonalInfo. */
@Composable
fun FamilyMemberRow(member: FamilyMember, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MemberImage(imageURL = member.imageURL, name = member.name, size = 44.dp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(text = member.name, color = OnSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            if (member.relationship.isNotBlank())
                Text(text = member.relationship, color = OnSurface.copy(alpha = 0.5f), fontSize = 13.sp)
        }
        Text(text = "›", color = OnSurface.copy(alpha = 0.3f), fontSize = 20.sp)
    }
}
