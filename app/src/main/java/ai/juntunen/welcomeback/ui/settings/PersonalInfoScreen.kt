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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.services.PhotoStorage
import ai.juntunen.welcomeback.ui.onboarding.OnboardingLabel
import ai.juntunen.welcomeback.ui.onboarding.OnboardingTextEditor
import ai.juntunen.welcomeback.ui.onboarding.OnboardingTextField
import ai.juntunen.welcomeback.ui.shared.MemberImage
import ai.juntunen.welcomeback.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PersonalInfoScreen(navController: NavController) {
    val lang    = LanguageManager
    val appVM: AppViewModel = viewModel()
    val profile by appVM.userProfile.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()

    var name      by remember(profile.name)      { mutableStateOf(profile.name) }
    var address   by remember(profile.address)   { mutableStateOf(profile.address) }
    var biography by remember(profile.biography) { mutableStateOf(profile.biography) }

    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { picked ->
            scope.launch {
                val token = withContext(Dispatchers.IO) {
                    PhotoStorage.savePhoto(context, picked, "profile")
                }
                if (token != null) appVM.updateProfileImageURL(token)
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
            TextButton(onClick = { navController.popBackStack() }) {
                Text(lang.t("common.cancel"), color = OnSurface.copy(alpha = 0.6f))
            }
            Text(
                text = lang.t("settings.personal.title"),
                color = OnSurface,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
            TextButton(onClick = {
                appVM.updateName(name.trim())
                appVM.updateAddress(address.trim())
                appVM.updateBiography(biography.trim())
                navController.popBackStack()
            }) {
                Text(lang.t("common.save"), color = AccentYellow, fontWeight = FontWeight.Bold)
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
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(2.dp, AccentYellow.copy(alpha = 0.6f), CircleShape)
                        .clickable {
                            photoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                ) {
                    MemberImage(
                        imageURL = profile.profileImageURL,
                        name = name.ifBlank { "?" },
                        size = 90.dp,
                        isCircle = true
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-12).dp)
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(AccentYellow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null,
                        tint = Color.Black, modifier = Modifier.size(14.dp))
                }
            }

            Spacer(Modifier.height(28.dp))

            OnboardingLabel(lang.t("settings.personal.name"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = lang.t("settings.personal.name.placeholder")
            )

            Spacer(Modifier.height(16.dp))
            OnboardingLabel(lang.t("settings.personal.address"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextField(
                value = address,
                onValueChange = { address = it },
                placeholder = lang.t("settings.personal.address.placeholder")
            )

            Spacer(Modifier.height(16.dp))
            OnboardingLabel(lang.t("settings.personal.biography"))
            Spacer(Modifier.height(8.dp))
            OnboardingTextEditor(
                value = biography,
                onValueChange = { biography = it },
                placeholder = lang.t("settings.personal.biography.placeholder"),
                minLines = 4
            )

            Spacer(Modifier.height(48.dp))
        }
    }
}
