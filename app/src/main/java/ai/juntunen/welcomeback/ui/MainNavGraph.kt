package ai.juntunen.welcomeback.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.AppTab
import ai.juntunen.welcomeback.ui.family.*
import ai.juntunen.welcomeback.ui.home.HomeScreen
import ai.juntunen.welcomeback.ui.listening.ListeningScreen
import ai.juntunen.welcomeback.ui.memories.*
import ai.juntunen.welcomeback.ui.music.MusicScreen
import ai.juntunen.welcomeback.ui.settings.*
import ai.juntunen.welcomeback.ui.theme.BackgroundDark

private data class TabItem(
    val tab: AppTab,
    val icon: ImageVector,
    val labelKey: String
)

private val TABS = listOf(
    TabItem(AppTab.HOME,      Icons.Filled.Home,        "tab.home"),
    TabItem(AppTab.MEMORIES,  Icons.Filled.PhotoLibrary,"tab.memories"),
    TabItem(AppTab.FAMILY,    Icons.Filled.People,      "tab.family"),
    TabItem(AppTab.MUSIC,     Icons.Filled.MusicNote,   "tab.music"),
    TabItem(AppTab.SETTINGS,  Icons.Filled.Settings,    "tab.settings"),
)

private fun AppTab.route() = name.lowercase()

/**
 * Root composable for the main app (post-onboarding). Hosts a 5-tab bottom
 * navigation bar and a full-screen `ListeningScreen` overlay when the mic is tapped.
 */
@Composable
fun MainAppScreen(onReset: () -> Unit = {}) {
    val lang   = LanguageManager
    val appVM: AppViewModel = viewModel()
    val selectedTab by appVM.selectedTab.collectAsStateWithLifecycle()
    val listeningOpen by appVM.listeningSheetPresented.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    // Keep NavController in sync with AppViewModel tab state
    LaunchedEffect(selectedTab) {
        val route = selectedTab.route()
        val current = navController.currentDestination?.route
        if (current != route) {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            // surfaceContainerLowest keeps the bar very dark and respects
            // future theme changes without a hard-coded hex colour.
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest) {
                TABS.forEach { item ->
                    val selected = selectedTab == item.tab
                    NavigationBarItem(
                        selected = selected,
                        onClick  = { appVM.selectTab(item.tab) },
                        icon  = { Icon(item.icon, contentDescription = lang.t(item.labelKey)) },
                        label = { Text(lang.t(item.labelKey), style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor   = MaterialTheme.colorScheme.primary,
                            selectedTextColor   = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor      = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = AppTab.HOME.route(),
            modifier = Modifier.padding(padding).fillMaxSize(),
            enterTransition = { fadeIn() },
            exitTransition  = { fadeOut() }
        ) {
            // ── Tabs ──────────────────────────────────────────────────
            composable(AppTab.HOME.route()) {
                LaunchedEffect(Unit) { appVM.selectTab(AppTab.HOME) }
                HomeScreen(
                    onMicTap    = { appVM.startListening() },
                    onMemberTap = { m -> navController.navigate("familyProfile/${m.id}") }
                )
            }

            composable(AppTab.MEMORIES.route()) {
                LaunchedEffect(Unit) { appVM.selectTab(AppTab.MEMORIES) }
                MemoriesScreen(
                    onMemberTap = { m -> navController.navigate("familyProfile/${m.id}") },
                    onPlaceTap  = { p -> navController.navigate("placeDetail/${p.id}") },
                    onMemoryTap = { m -> navController.navigate("memoryStoryDetail/${m.id}") }
                )
            }

            composable(AppTab.FAMILY.route()) {
                LaunchedEffect(Unit) { appVM.selectTab(AppTab.FAMILY) }
                FamilyScreen(onMemberTap = { m -> navController.navigate("familyProfile/${m.id}") })
            }

            composable(AppTab.MUSIC.route()) {
                LaunchedEffect(Unit) { appVM.selectTab(AppTab.MUSIC) }
                MusicScreen()
            }

            composable(AppTab.SETTINGS.route()) {
                LaunchedEffect(Unit) { appVM.selectTab(AppTab.SETTINGS) }
                SettingsScreen(navController = navController, onReset = onReset)
            }

            // ── Detail screens ────────────────────────────────────────
            composable(
                route = "familyProfile/{memberId}",
                arguments = listOf(navArgument("memberId") { type = NavType.StringType })
            ) { back ->
                val memberId = back.arguments?.getString("memberId") ?: return@composable
                val profile by appVM.userProfile.collectAsStateWithLifecycle()
                val member = profile.familyMembers.find { it.id == memberId } ?: return@composable
                FamilyMemberProfileScreen(member = member, onBack = { navController.popBackStack() })
            }

            composable(
                route = "familyDetail/{memberIdOrNew}",
                arguments = listOf(navArgument("memberIdOrNew") { type = NavType.StringType })
            ) { back ->
                val id = back.arguments?.getString("memberIdOrNew") ?: "new"
                val profile by appVM.userProfile.collectAsStateWithLifecycle()
                val existing = if (id == "new") null else profile.familyMembers.find { it.id == id }
                FamilyMemberDetailScreen(
                    existing  = existing,
                    onDismiss = { navController.popBackStack() }
                )
            }

            composable(
                route = "memoryStoryDetail/{memoryId}",
                arguments = listOf(navArgument("memoryId") { type = NavType.StringType })
            ) { back ->
                val memoryId = back.arguments?.getString("memoryId") ?: return@composable
                val profile by appVM.userProfile.collectAsStateWithLifecycle()
                val memory = profile.memories.find { it.id == memoryId } ?: return@composable
                MemoryStoryDetailScreen(memory = memory, onBack = { navController.popBackStack() })
            }

            composable(
                route = "placeDetail/{placeId}",
                arguments = listOf(navArgument("placeId") { type = NavType.StringType })
            ) { back ->
                val placeId = back.arguments?.getString("placeId") ?: return@composable
                val profile by appVM.userProfile.collectAsStateWithLifecycle()
                val place = profile.places.find { it.id == placeId } ?: return@composable
                PlaceDetailScreen(place = place, onBack = { navController.popBackStack() })
            }

            // ── Settings sub-screens ──────────────────────────────────
            composable("settings/personal")      { PersonalInfoScreen(navController) }
            composable("settings/family")        { FamilyManagementScreen(navController) }
            composable("settings/memories")      { MemoriesManagementScreen(navController) }
            composable("settings/places")        { PlacesManagementScreen(navController) }
            composable("settings/notifications") { NotificationsSettingsScreen(navController) }
            composable("settings/language")      { LanguageSettingsScreen(navController) }
            composable("settings/voiceMode")     { VoiceModeSettingsScreen(navController) }
            composable("settings/model")         { ModelSettingsScreen(navController) }
            composable(
                route = "settings/legal/{document}",
                arguments = listOf(navArgument("document") { type = NavType.StringType })
            ) { back ->
                val doc = back.arguments?.getString("document") ?: "privacy"
                LegalScreen(document = doc, onBack = { navController.popBackStack() })
            }

            composable(
                route = "settings/memoryDetail/{memoryIdOrNew}",
                arguments = listOf(navArgument("memoryIdOrNew") { type = NavType.StringType })
            ) { back ->
                val id = back.arguments?.getString("memoryIdOrNew") ?: "new"
                MemoryDetailEditScreen(memoryId = id, onDismiss = { navController.popBackStack() })
            }

            composable(
                route = "settings/placeDetail/{placeIdOrNew}",
                arguments = listOf(navArgument("placeIdOrNew") { type = NavType.StringType })
            ) { back ->
                val id = back.arguments?.getString("placeIdOrNew") ?: "new"
                PlaceDetailEditScreen(placeId = id, onDismiss = { navController.popBackStack() })
            }
        }
    }

    // Full-screen listening overlay
    if (listeningOpen) {
        ListeningScreen(onDismiss = { appVM.stopListening() })
    }
}
