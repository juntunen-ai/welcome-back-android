package ai.juntunen.welcomeback.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.NotificationTime
import ai.juntunen.welcomeback.services.NotificationService
import ai.juntunen.welcomeback.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsSettingsScreen(navController: NavController) {
    val lang    = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(lang.t("settings.notifications.title"), color = OnSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = OnSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Enable toggle
            SettingsGroup {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(32.dp).clip(MaterialTheme.shapes.small)
                            .background(Color(0xFFFFB74D).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Notifications, contentDescription = null,
                            tint = Color(0xFFFFB74D), modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(lang.t("settings.notifications.enable"), color = OnSurface, fontSize = 15.sp)
                        Text(
                            lang.t("settings.notifications.enable.subtitle"),
                            color = OnSurface.copy(alpha = 0.45f), fontSize = 12.sp
                        )
                    }
                    Switch(
                        checked = profile.notificationsEnabled,
                        onCheckedChange = { enabled ->
                            appVM.updateNotificationsEnabled(enabled)
                            // Schedule or cancel notifications
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = AccentYellow
                        )
                    )
                }
            }

            if (profile.notificationsEnabled) {
                Spacer(Modifier.height(20.dp))
                SettingsSectionHeader(lang.t("settings.notifications.times"))

                Spacer(Modifier.height(8.dp))
                SettingsGroup {
                    NotificationTime.allCases.forEachIndexed { idx, time ->
                        val isSelected = time in profile.notificationTimes
                        val label = when (time) {
                            NotificationTime.MORNING   -> lang.t("settings.notifications.morning")
                            NotificationTime.NOON      -> lang.t("settings.notifications.noon")
                            NotificationTime.AFTERNOON -> lang.t("settings.notifications.afternoon")
                            NotificationTime.EVENING   -> lang.t("settings.notifications.evening")
                        }
                        val hour = "%02d:00".format(time.hour)
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(label, color = OnSurface, fontSize = 15.sp)
                                Text(hour, color = OnSurface.copy(alpha = 0.45f), fontSize = 12.sp)
                            }
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { checked ->
                                    val updated = if (checked)
                                        profile.notificationTimes + time
                                    else
                                        profile.notificationTimes.filter { it != time }
                                    appVM.updateNotificationTimes(updated)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AccentYellow,
                                    checkmarkColor = Color.Black
                                )
                            )
                        }
                        if (idx < NotificationTime.allCases.lastIndex) SettingsDivider()
                    }
                }

                Spacer(Modifier.height(20.dp))
                SettingsSectionHeader(lang.t("settings.notifications.topics.header"))
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = profile.notificationTopics,
                    onValueChange = { appVM.updateNotificationTopics(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(lang.t("settings.notifications.topics.placeholder"),
                        color = OnSurface.copy(alpha = 0.3f)) },
                    textStyle = androidx.compose.ui.text.TextStyle(color = OnSurface, fontSize = 15.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentYellow.copy(alpha = 0.6f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    shape = MaterialTheme.shapes.medium,
                    minLines = 2
                )
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}
