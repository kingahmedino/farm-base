package com.farmbase.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.farmbase.app.R

val EpilogueFontFamily = FontFamily(
    Font(R.font.epilogue_regular, FontWeight.Normal),
    Font(R.font.epilogue_medium, FontWeight.Medium),
    Font(R.font.epilogue_bold, FontWeight.Bold),
)

val baseline = Typography()

val Typography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = EpilogueFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = EpilogueFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = EpilogueFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = EpilogueFontFamily, fontWeight = FontWeight.SemiBold),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = EpilogueFontFamily, fontWeight = FontWeight.SemiBold),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = EpilogueFontFamily, fontWeight = FontWeight.SemiBold),
    titleLarge = baseline.titleLarge.copy(fontFamily = EpilogueFontFamily, fontWeight = FontWeight.SemiBold),
    titleMedium = baseline.titleMedium.copy(fontFamily = EpilogueFontFamily, fontWeight = FontWeight.SemiBold),
    titleSmall = baseline.titleSmall.copy(fontFamily = EpilogueFontFamily, fontWeight = FontWeight.Medium),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = EpilogueFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = EpilogueFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = EpilogueFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = EpilogueFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = EpilogueFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = EpilogueFontFamily),
)