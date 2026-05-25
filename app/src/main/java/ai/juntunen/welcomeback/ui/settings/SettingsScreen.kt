package ai.juntunen.welcomeback.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.shared.MemberImage
import ai.juntunen.welcomeback.ui.theme.*

@Composable
fun SettingsScreen(navController: NavController, onReset: () -> Unit = {}) {
    val lang  = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsStateWithLifecycle()
    var showResetConfirm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BackgroundDark),
        contentPadding = PaddingValues(bottom = 40.dp)
    ) {
        // Header
        item {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 56.dp, bottom = 20.dp)
            ) {
                Text(
                    text = lang.t("settings.title"),
                    color = OnSurface,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Profile card
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .clickable { navController.navigate("settings/personal") }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MemberImage(
                    imageURL = profile.profileImageURL,
                    name = profile.name.ifBlank { "?" },
                    size = 56.dp,
                    isCircle = true,
                    modifier = Modifier.border(2.dp, AccentYellow.copy(alpha = 0.6f), CircleShape)
                )
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = profile.name.ifBlank { lang.t("settings.profile.unnamed") },
                        color = OnSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = lang.t("settings.profile.edit"),
                        color = AccentYellow,
                        fontSize = 13.sp
                    )
                }
                Icon(Icons.Filled.ChevronRight, contentDescription = null,
                    tint = OnSurface.copy(alpha = 0.3f), modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.height(24.dp))
        }

        // ── Content section ─────────────────────────────────────────
        item {
            SettingsSectionHeader(lang.t("settings.section.content"))
        }
        item {
            SettingsGroup {
                SettingsRow(
                    icon = Icons.Filled.People,
                    iconTint = Color(0xFF4FC3F7),
                    title = lang.t("settings.family"),
                    subtitle = lang.t("settings.family.count", profile.familyMembers.size),
                    onClick = { navController.navigate("settings/family") }
                )
                SettingsDivider()
                SettingsRow(
                    icon = Icons.Filled.PhotoLibrary,
                    iconTint = Color(0xFFA5D6A7),
                    title = lang.t("settings.memories"),
                    subtitle = lang.t("settings.memories.count", profile.memories.size),
                    onClick = { navController.navigate("settings/memories") }
                )
                SettingsDivider()
                SettingsRow(
                    icon = Icons.Filled.Place,
                    iconTint = AccentYellow,
                    title = lang.t("settings.places"),
                    subtitle = lang.t("settings.places.count", profile.places.size),
                    onClick = { navController.navigate("settings/places") }
                )
            }
            Spacer(Modifier.height(20.dp))
        }

        // ── Preferences section ──────────────────────────────────────
        item {
            SettingsSectionHeader(lang.t("settings.section.preferences"))
        }
        item {
            SettingsGroup {
                SettingsRow(
                    icon = Icons.Filled.Notifications,
                    iconTint = Color(0xFFFFB74D),
                    title = lang.t("settings.notifications"),
                    onClick = { navController.navigate("settings/notifications") }
                )
                SettingsDivider()
                SettingsRow(
                    icon = Icons.Filled.Language,
                    iconTint = Color(0xFF80CBC4),
                    title = lang.t("settings.language"),
                    subtitle = LanguageManager.language.displayName,
                    onClick = { navController.navigate("settings/language") }
                )
                SettingsDivider()
                SettingsRow(
                    icon = Icons.Filled.Mic,
                    iconTint = Color(0xFFCE93D8),
                    title = lang.t("settings.voice.mode"),
                    onClick = { navController.navigate("settings/voiceMode") }
                )
                SettingsDivider()
                SettingsRow(
                    icon = Icons.Filled.Psychology,
                    iconTint = Color(0xFF80DEEA),
                    title = lang.t("settings.model"),
                    onClick = { navController.navigate("settings/model") }
                )
            }
            Spacer(Modifier.height(20.dp))
        }

        // ── Legal section ────────────────────────────────────────────
        item {
            SettingsSectionHeader(lang.t("settings.section.legal"))
        }
        item {
            SettingsGroup {
                SettingsRow(
                    icon = Icons.Filled.PrivacyTip,
                    iconTint = OnSurface.copy(alpha = 0.5f),
                    title = lang.t("settings.privacy"),
                    onClick = { navController.navigate("settings/legal/privacy") }
                )
                SettingsDivider()
                SettingsRow(
                    icon = Icons.Filled.Description,
                    iconTint = OnSurface.copy(alpha = 0.5f),
                    title = lang.t("settings.terms"),
                    onClick = { navController.navigate("settings/legal/terms") }
                )
            }
            Spacer(Modifier.height(20.dp))
        }

        // ── Danger zone ──────────────────────────────────────────────
        item {
            SettingsGroup {
                SettingsRow(
                    icon = Icons.Filled.RestartAlt,
                    iconTint = Color(0xFFFF6B6B),
                    title = lang.t("settings.reset"),
                    titleColor = Color(0xFFFF6B6B),
                    onClick = { showResetConfirm = true }
                )
            }
            Spacer(Modifier.height(12.dp))
        }

        // Version
        item {
            Text(
                text = lang.t("settings.version", "0.2.0"),
                color = OnSurface.copy(alpha = 0.25f),
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            )
        }
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text(lang.t("settings.reset.confirm.title"), color = OnSurface) },
            text  = { Text(lang.t("settings.reset.confirm.body"), color = OnSurface.copy(0.7f)) },
            confirmButton = {
                TextButton(onClick = { showResetConfirm = false; onReset() }) {
                    Text(lang.t("settings.reset"), color = Color(0xFFFF6B6B))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text(lang.t("common.cancel"))
                }
            },
            containerColor = SurfaceVariant
        )
    }
}

// ── Shared sub-components ────────────────────────────────────────────────────

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        color = OnSurface.copy(alpha = 0.4f),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
    )
}

/**
 * Rounded card container for a group of [SettingsRow] items.
 * Uses [MaterialTheme.colorScheme.surfaceContainerHigh] — the correct M3 token
 * for an elevated card on a dark background.
 */
@Composable
fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        content = content
    )
}

/**
 * M3 [ListItem]-based settings row.
 *
 * Using [ListItem] instead of a hand-rolled Row gives:
 *   • Correct M3 padding / minimum touch-target sizing.
 *   • Automatic semantic content description slots.
 *   • Consistent leading / trailing content alignment.
 */
@Composable
fun SettingsRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String? = null,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    ListItem(
        headlineContent = {
            Text(title, color = titleColor, style = MaterialTheme.typography.bodyLarge)
        },
        supportingContent = subtitle?.let { sub ->
            { Text(sub, style = MaterialTheme.typography.bodyMedium) }
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
        },
        trailingContent = trailingContent ?: if (onClick != null) {
            {
                Icon(
                    Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(18.dp)
                )
            }
        } else null,
        modifier = Modifier.then(
            if (onClick != null) Modifier.clickable { onClick() } else Modifier
        ),
        colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
    )
}

@Composable
fun SettingsDivider() {
    HorizontalDivider(
        modifier  = Modifier.padding(start = 60.dp),
        color     = MaterialTheme.colorScheme.outlineVariant
    )
}
