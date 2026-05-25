package ai.juntunen.welcomeback.ui.settings

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.Place
import ai.juntunen.welcomeback.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesManagementScreen(navController: NavController) {
    val lang  = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(lang.t("settings.places.title"), color = OnSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = OnSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("settings/placeDetail/new") },
                containerColor = AccentYellow,
                contentColor = Color.Black
            ) {
                Icon(Icons.Filled.Add, contentDescription = lang.t("common.add"))
            }
        }
    ) { padding ->
        if (profile.places.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(40.dp)) {
                    Icon(Icons.Filled.Place, contentDescription = null,
                        tint = OnSurface.copy(alpha = 0.2f), modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text(lang.t("settings.places.empty"), color = OnSurface.copy(alpha = 0.4f),
                        fontSize = 16.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(profile.places, key = { it.id }) { place ->
                    PlaceManagementRow(
                        place = place,
                        onClick = { navController.navigate("settings/placeDetail/${place.id}") }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun PlaceManagementRow(place: Place, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(MaterialTheme.shapes.small)
                .background(AccentYellow.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Place, contentDescription = null,
                tint = AccentYellow, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(text = place.name, color = OnSurface, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            if (place.description.isNotBlank()) {
                Text(
                    text = place.description,
                    color = OnSurface.copy(alpha = 0.45f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null,
            tint = OnSurface.copy(alpha = 0.25f), modifier = Modifier.size(18.dp))
    }
}
