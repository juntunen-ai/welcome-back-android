package ai.juntunen.welcomeback.ui.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.Place
import ai.juntunen.welcomeback.services.PhotoStorage
import ai.juntunen.welcomeback.ui.onboarding.OnboardingLabel
import ai.juntunen.welcomeback.ui.onboarding.OnboardingTextEditor
import ai.juntunen.welcomeback.ui.onboarding.OnboardingTextField
import ai.juntunen.welcomeback.ui.theme.*
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PlaceDetailEditScreen(placeId: String, onDismiss: () -> Unit) {
    val lang    = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()

    val isNew    = placeId == "new"
    val existing = if (isNew) null else profile.places.find { it.id == placeId }
    var draft    by remember { mutableStateOf(existing ?: Place(name = "")) }

    val canSave = draft.name.trim().isNotEmpty()

    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { picked ->
            scope.launch {
                val token = withContext(Dispatchers.IO) {
                    PhotoStorage.savePhoto(context, picked, draft.id)
                }
                if (token != null) draft = draft.copy(imageURL = token)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Toolbar
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onDismiss) {
                Text(lang.t("common.cancel"), color = OnSurface.copy(alpha = 0.6f))
            }
            Text(
                text = if (isNew) lang.t("settings.place.add.title") else lang.t("settings.place.edit.title"),
                color = OnSurface, fontSize = 17.sp, fontWeight = FontWeight.SemiBold
            )
            TextButton(
                onClick = {
                    if (!canSave) return@TextButton
                    val trimmed = draft.copy(name = draft.name.trim(), description = draft.description.trim())
                    if (isNew) appVM.addPlace(trimmed) else appVM.updatePlace(trimmed)
                    onDismiss()
                },
                enabled = canSave
            ) {
                Text(lang.t("common.save"),
                    color = if (canSave) AccentYellow else OnSurface.copy(alpha = 0.3f),
                    fontWeight = FontWeight.Bold)
            }
        }

        HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            // Photo picker
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), MaterialTheme.shapes.large)
                    .clickable {
                        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentAlignment = Alignment.Center
            ) {
                val file = if (draft.imageURL.isNotBlank()) PhotoStorage.fileFor(context, draft.imageURL) else null
                if (file != null) {
                    AsyncImage(
                        model = file,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.large)
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Image, contentDescription = null,
                            tint = AccentYellow.copy(alpha = 0.5f), modifier = Modifier.size(36.dp))
                        Spacer(Modifier.height(6.dp))
                        Text(lang.t("settings.place.photo.hint"),
                            color = OnSurface.copy(alpha = 0.4f), fontSize = 13.sp)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            OnboardingLabel(lang.t("settings.place.name.label"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextField(
                value = draft.name,
                onValueChange = { draft = draft.copy(name = it) },
                placeholder = lang.t("settings.place.name.placeholder")
            )

            Spacer(Modifier.height(16.dp))
            OnboardingLabel(lang.t("settings.place.description.label"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextEditor(
                value = draft.description,
                onValueChange = { draft = draft.copy(description = it) },
                placeholder = lang.t("settings.place.description.placeholder"),
                minLines = 4
            )

            // Delete (edit mode only)
            if (!isNew) {
                Spacer(Modifier.height(32.dp))
                var showConfirm by remember { mutableStateOf(false) }
                OutlinedButton(
                    onClick = { showConfirm = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF4444)),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFF4444).copy(alpha = 0.5f))
                    )
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(lang.t("settings.place.delete"))
                }
                if (showConfirm) {
                    AlertDialog(
                        onDismissRequest = { showConfirm = false },
                        title = { Text(lang.t("settings.place.delete.confirm.title"), color = OnSurface) },
                        text  = { Text(lang.t("settings.place.delete.confirm.body"), color = OnSurface.copy(0.7f)) },
                        confirmButton = {
                            TextButton(onClick = { appVM.deletePlace(draft.id); onDismiss() }) {
                                Text(lang.t("common.delete"), color = Color(0xFFFF4444))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showConfirm = false }) { Text(lang.t("common.cancel")) }
                        },
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                }
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}
