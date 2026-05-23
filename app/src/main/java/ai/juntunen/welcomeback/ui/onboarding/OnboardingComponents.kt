package ai.juntunen.welcomeback.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.juntunen.welcomeback.ui.theme.AccentYellow
import ai.juntunen.welcomeback.ui.theme.OnSurface
import ai.juntunen.welcomeback.ui.theme.SurfaceVariant

/**
 * Tiny SECTION LABEL above each input — uppercase, 12 sp, semibold, .8 sp tracking.
 * Matches the iOS visual language used on every onboarding form.
 */
@Composable
fun OnboardingLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        color = OnSurface.copy(alpha = 0.45f),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.8.sp,
        modifier = modifier.padding(start = 4.dp)
    )
}

/** Single-line input styled to match the iOS onboarding fields. */
@Composable
fun OnboardingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Words,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = OnSurface.copy(alpha = 0.3f),
                fontSize = 18.sp
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(capitalization = capitalization),
        colors = TextFieldDefaults.colors(
            focusedContainerColor   = SurfaceVariant.copy(alpha = 0.4f),
            unfocusedContainerColor = SurfaceVariant.copy(alpha = 0.4f),
            focusedTextColor   = OnSurface,
            unfocusedTextColor = OnSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = AccentYellow,
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
    )
}

/** Multi-line text editor for memory / story / description fields. */
@Composable
fun OnboardingTextEditor(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    minLines: Int = 5
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = OnSurface.copy(alpha = 0.3f),
                fontSize = 16.sp
            )
        },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        minLines = minLines,
        colors = TextFieldDefaults.colors(
            focusedContainerColor   = SurfaceVariant.copy(alpha = 0.4f),
            unfocusedContainerColor = SurfaceVariant.copy(alpha = 0.4f),
            focusedTextColor   = OnSurface,
            unfocusedTextColor = OnSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = AccentYellow,
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
    )
}

/** Main yellow CTA used by every onboarding screen. */
@Composable
fun OnboardingPrimaryButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentYellow,
            contentColor   = Color.Black,
            disabledContainerColor = SurfaceVariant.copy(alpha = 0.5f),
            disabledContentColor   = OnSurface.copy(alpha = 0.3f),
        ),
        shape = CircleShape,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(text = text, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

/** Small underlined "Skip for now" / "Add later" link. */
@Composable
fun OnboardingSkipLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = text,
            color = OnSurface.copy(alpha = 0.5f),
            fontSize = 15.sp,
            textDecoration = TextDecoration.Underline
        )
    }
}

/** "Settings → X" pill shown on each Tip screen. */
@Composable
fun OnboardingSettingsPill(label: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceVariant.copy(alpha = 0.4f))
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "⚙",
            color = AccentYellow,
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 10.dp)
        )
        Text(
            text = label,
            color = OnSurface.copy(alpha = 0.7f),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}
