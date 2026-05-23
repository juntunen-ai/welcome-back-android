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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.Memory
import ai.juntunen.welcomeback.data.MemoryCategory
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
fun MemoryDetailEditScreen(memoryId: String, onDismiss: () -> Unit) {
    val lang    = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()

    val isNew = memoryId == "new"
    val existing = if (isNew) null else profile.memories.find { it.id == memoryId }
    var draft by remember { mutableStateOf(existing ?: Memory(title = "")) }

    val canSave = draft.title.trim().isNotEmpty()

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

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
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
                text = if (isNew) lang.t("settings.memory.add.title") else lang.t("settings.memory.edit.title"),
                color = OnSurface, fontSize = 17.sp, fontWeight = FontWeight.SemiBold
            )
            TextButton(
                onClick = {
                    if (!canSave) return@TextButton
                    val trimmed = draft.copy(title = draft.title.trim(), description = draft.description.trim())
                    if (isNew) appVM.addMemory(trimmed) else appVM.updateMemory(trimmed)
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
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceVariant.copy(alpha = 0.4f))
                    .border(1.dp, AccentYellow.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
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
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp))
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Image, contentDescription = null,
                            tint = AccentYellow.copy(alpha = 0.5f), modifier = Modifier.size(36.dp))
                        Spacer(Modifier.height(6.dp))
                        Text(lang.t("settings.memory.photo.hint"),
                            color = OnSurface.copy(alpha = 0.4f), fontSize = 13.sp)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            OnboardingLabel(lang.t("settings.memory.title.label"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextField(
                value = draft.title,
                onValueChange = { draft = draft.copy(title = it) },
                placeholder = lang.t("settings.memory.title.placeholder")
            )

            Spacer(Modifier.height(16.dp))
            OnboardingLabel(lang.t("settings.memory.date.label"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextField(
                value = draft.date,
                onValueChange = { draft = draft.copy(date = it) },
                placeholder = lang.t("settings.memory.date.placeholder")
            )

            Spacer(Modifier.height(16.dp))
            OnboardingLabel(lang.t("settings.memory.category.label"))
            Spacer(Modifier.height(8.dp))
            CategorySelector(
                selected = draft.category,
                onSelect = { draft = draft.copy(category = it) }
            )

            Spacer(Modifier.height(16.dp))
            OnboardingLabel(lang.t("settings.memory.description.label"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextEditor(
                value = draft.description,
                onValueChange = { draft = draft.copy(description = it) },
                placeholder = lang.t("settings.memory.description.placeholder"),
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
                    Text(lang.t("settings.memory.delete"))
                }
                if (showConfirm) {
                    AlertDialog(
                        onDismissRequest = { showConfirm = false },
                        title = { Text(lang.t("settings.memory.delete.confirm.title"), color = OnSurface) },
                        text  = { Text(lang.t("settings.memory.delete.confirm.body"), color = OnSurface.copy(0.7f)) },
                        confirmButton = {
                            TextButton(onClick = { appVM.deleteMemory(draft.id); onDismiss() }) {
                                Text(lang.t("common.delete"), color = Color(0xFFFF4444))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showConfirm = false }) { Text(lang.t("common.cancel")) }
                        },
                        containerColor = SurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun CategorySelector(selected: MemoryCategory, onSelect: (MemoryCategory) -> Unit) {
    val lang = LanguageManager
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        MemoryCategory.entries.forEach { cat ->
            val label = when (cat) {
                MemoryCategory.FAMILY -> lang.t("memory.category.family")
                MemoryCategory.EVENTS -> lang.t("memory.category.events")
                MemoryCategory.PLACES -> lang.t("memory.category.places")
                MemoryCategory.OTHER  -> lang.t("memory.category.other")
            }
            val isSelected = selected == cat
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) AccentYellow.copy(alpha = 0.18f) else SurfaceVariant.copy(alpha = 0.4f))
                    .border(
                        1.dp,
                        if (isSelected) AccentYellow.copy(alpha = 0.6f) else Color.Transparent,
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { onSelect(cat) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = if (isSelected) AccentYellow else OnSurface.copy(alpha = 0.55f),
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
