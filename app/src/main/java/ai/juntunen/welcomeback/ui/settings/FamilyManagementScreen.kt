package ai.juntunen.welcomeback.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.family.FamilyMemberRow
import ai.juntunen.welcomeback.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyManagementScreen(navController: NavController) {
    val lang  = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text(lang.t("settings.family.title"), color = OnSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = OnSurface)
                    }
                },
                actions = {
                    TextButton(onClick = { navController.navigate("familyDetail/new") }) {
                        Text(lang.t("common.add"), color = AccentYellow, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        },
        floatingActionButton = {
            if (profile.familyMembers.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { navController.navigate("familyDetail/new") },
                    containerColor = AccentYellow,
                    contentColor = androidx.compose.ui.graphics.Color.Black
                ) {
                    Icon(Icons.Filled.Add, contentDescription = lang.t("common.add"))
                }
            }
        }
    ) { padding ->
        if (profile.familyMembers.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(40.dp)) {
                    Icon(Icons.Filled.People, contentDescription = null,
                        tint = OnSurface.copy(alpha = 0.2f), modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text(lang.t("settings.family.empty"), color = OnSurface.copy(alpha = 0.4f),
                        fontSize = 16.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = { navController.navigate("familyDetail/new") },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentYellow,
                            contentColor = androidx.compose.ui.graphics.Color.Black)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(lang.t("settings.family.add"), fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(profile.familyMembers, key = { it.id }) { member ->
                    FamilyMemberRow(
                        member = member,
                        onClick = { navController.navigate("familyDetail/${member.id}") }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}
