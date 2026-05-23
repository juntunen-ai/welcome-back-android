package ai.juntunen.welcomeback.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.juntunen.welcomeback.LanguageManager
import ai.juntunen.welcomeback.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalScreen(document: String, onBack: () -> Unit) {
    val lang = LanguageManager

    val title = if (document == "privacy") lang.t("settings.privacy") else lang.t("settings.terms")

    val body = if (document == "privacy") {
        """
Welcome Back Privacy Policy

Last updated: 2026-05-01

1. Data Storage
All personal information — your name, biography, family details, memories, and photos — is stored exclusively on your device. We do not transmit your personal data to external servers.

2. AI Processing
When you use the voice conversation feature, your spoken text is sent to Google's Gemini API for processing. This is the only data that leaves your device, and it is governed by Google's Privacy Policy.

3. No Analytics
We do not collect any analytics, crash reports, or usage data. There are no third-party SDKs collecting data in this app.

4. Photos
Photos you add are stored in the app's private internal storage and are never shared externally.

5. Notifications
If you enable notifications, they are generated locally on your device using WorkManager. No notification content is sent to external servers.

6. Contact
For privacy questions: harri.newsletter@gofore.com
        """.trimIndent()
    } else {
        """
Welcome Back Terms of Use

Last updated: 2026-05-01

1. Acceptance
By using Welcome Back, you agree to these terms.

2. License
Welcome Back is proprietary software. Copyright © 2026 Harri Juntunen. All Rights Reserved.

3. Permitted Use
You may use this app for personal, non-commercial purposes only. You may not copy, modify, distribute, or create derivative works.

4. Medical Disclaimer
Welcome Back is a memory aid tool and is not a medical device. It does not provide medical advice or replace professional care for dementia or other conditions.

5. No Warranty
This app is provided "as is" without any warranty. We do not guarantee uninterrupted or error-free operation.

6. Limitation of Liability
To the maximum extent permitted by law, we are not liable for any damages arising from use of this app.

7. Contact
harri.newsletter@gofore.com
        """.trimIndent()
    }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text(title, color = OnSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = OnSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceVariant.copy(alpha = 0.3f))
                    .padding(18.dp)
            ) {
                Text(
                    text = body,
                    color = OnSurface.copy(alpha = 0.75f),
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}
