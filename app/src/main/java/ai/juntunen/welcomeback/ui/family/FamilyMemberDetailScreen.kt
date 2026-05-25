package ai.juntunen.welcomeback.ui.family

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.data.FamilyMember
import ai.juntunen.welcomeback.services.PhotoStorage
import ai.juntunen.welcomeback.ui.onboarding.OnboardingLabel
import ai.juntunen.welcomeback.ui.onboarding.OnboardingPrimaryButton
import ai.juntunen.welcomeback.ui.onboarding.OnboardingTextField
import ai.juntunen.welcomeback.ui.onboarding.OnboardingTextEditor
import ai.juntunen.welcomeback.ui.shared.MemberImage
import ai.juntunen.welcomeback.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

@Composable
fun FamilyMemberDetailScreen(
    existing: FamilyMember?,
    onDismiss: () -> Unit
) {
    val lang    = LanguageManager
    val appVM: AppViewModel = viewModel()
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()

    val isEdit = existing != null
    var draft  by remember { mutableStateOf(existing ?: FamilyMember(name = "", relationship = "")) }
    var isSaving by remember { mutableStateOf(false) }

    val canSave = draft.name.trim().isNotEmpty()

    // Photo picker
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Toolbar
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onDismiss) {
                Text(lang.t("cancel"), color = OnSurface.copy(alpha = 0.6f))
            }
            Text(
                text = if (isEdit) lang.t("family.edit.title") else lang.t("family.add.title"),
                color = OnSurface,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
            TextButton(
                onClick = {
                    if (!canSave) return@TextButton
                    if (isEdit) appVM.updateFamilyMember(draft.copy(name = draft.name.trim(), relationship = draft.relationship.trim()))
                    else        appVM.addFamilyMember(draft.copy(name = draft.name.trim(), relationship = draft.relationship.trim()))
                    onDismiss()
                },
                enabled = canSave
            ) {
                Text(lang.t("save"), color = if (canSave) AccentYellow else OnSurface.copy(alpha = 0.3f),
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
            Spacer(Modifier.height(24.dp))

            // Profile photo
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, AccentYellow.copy(alpha = 0.6f), CircleShape)
                        .clickable {
                            photoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                ) {
                    MemberImage(
                        imageURL = draft.imageURL,
                        name = draft.name,
                        size = 100.dp,
                        isCircle = true
                    )
                }
                // Camera icon overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-16).dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(AccentYellow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null,
                        tint = Color.Black, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(Modifier.height(28.dp))

            OnboardingLabel(lang.t("family.add.name"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextField(
                value = draft.name,
                onValueChange = { draft = draft.copy(name = it) },
                placeholder = lang.t("family.add.name.placeholder")
            )

            Spacer(Modifier.height(16.dp))
            OnboardingLabel(lang.t("family.add.relationship"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextField(
                value = draft.relationship,
                onValueChange = { draft = draft.copy(relationship = it) },
                placeholder = lang.t("family.add.relationship.placeholder")
            )

            Spacer(Modifier.height(16.dp))
            OnboardingLabel(lang.t("family.add.phone"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextField(
                value = draft.phone,
                onValueChange = { draft = draft.copy(phone = it) },
                placeholder = lang.t("family.add.phone.placeholder"),
                capitalization = KeyboardCapitalization.None
            )

            Spacer(Modifier.height(16.dp))
            OnboardingLabel(lang.t("family.add.biography"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextEditor(
                value = draft.biography,
                onValueChange = { draft = draft.copy(biography = it) },
                placeholder = lang.t("family.add.biography.placeholder"),
                minLines = 3
            )

            Spacer(Modifier.height(16.dp))
            OnboardingLabel(lang.t("family.add.memory1"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextEditor(
                value = draft.memory1,
                onValueChange = { draft = draft.copy(memory1 = it) },
                placeholder = lang.t("family.add.memory1.placeholder"),
                minLines = 3
            )

            Spacer(Modifier.height(16.dp))
            OnboardingLabel(lang.t("family.add.memory2"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextEditor(
                value = draft.memory2,
                onValueChange = { draft = draft.copy(memory2 = it) },
                placeholder = lang.t("family.add.memory2.placeholder"),
                minLines = 3
            )

            // Delete button in edit mode
            if (isEdit) {
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
                    Text(lang.t("family.edit.delete"))
                }

                if (showConfirm) {
                    AlertDialog(
                        onDismissRequest = { showConfirm = false },
                        title = { Text(lang.t("family.edit.delete.confirm.title"), color = OnSurface) },
                        text  = { Text(lang.t("family.edit.delete.confirm.body"), color = OnSurface.copy(0.7f)) },
                        confirmButton = {
                            TextButton(onClick = {
                                appVM.deleteFamilyMember(draft.id)
                                onDismiss()
                            }) { Text(lang.t("delete"), color = Color(0xFFFF4444)) }
                        },
                        dismissButton = {
                            TextButton(onClick = { showConfirm = false }) { Text(lang.t("cancel")) }
                        },
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                }
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}
