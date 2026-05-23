package ai.juntunen.welcomeback.ui.onboarding

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ai.juntunen.welcomeback.AppViewModel
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.services.PhotoStorage
import ai.juntunen.welcomeback.ui.theme.AccentYellow
import ai.juntunen.welcomeback.ui.theme.OnSurface
import ai.juntunen.welcomeback.ui.theme.SurfaceVariant

@Composable
fun OnboardingPhotoScreen(onContinue: () -> Unit) {
    val lang = LanguageManager
    val appVM: AppViewModel = viewModel()
    val context = LocalContext.current

    var pickedUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> if (uri != null) pickedUri = uri }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(SurfaceVariant)
                .border(
                    width = 3.dp,
                    color = if (pickedUri != null) AccentYellow else Color.White.copy(alpha = 0.15f),
                    shape = CircleShape
                )
                .clickable {
                    launcher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            val uri = pickedUri
            if (uri != null) {
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Text(text = "📷", fontSize = 52.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = lang.t("onboarding.photo.tap"),
            color = OnSurface.copy(alpha = 0.4f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = lang.t("onboarding.photo.title"),
            color = OnSurface,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = lang.t("onboarding.photo.subtitle"),
            color = OnSurface.copy(alpha = 0.6f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        OnboardingPrimaryButton(
            text = lang.t("onboarding.photo.continue"),
            onClick = {
                pickedUri?.let { uri ->
                    val token = PhotoStorage.savePhoto(context, uri, "user_profile")
                    if (token != null) appVM.updateProfileImageURL(token)
                }
                onContinue()
            }
        )
        OnboardingSkipLink(text = lang.t("onboarding.photo.skip"), onClick = onContinue)
        Spacer(modifier = Modifier.height(40.dp))
    }
}
