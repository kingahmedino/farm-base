package com.farmbase.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
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

val BloggerSansFontFamily = FontFamily(
    Font(R.font.blogger_sans_regular, FontWeight.Normal),
    Font(R.font.blogger_sans_medium, FontWeight.Medium),
    Font(R.font.blogger_sans_bold, FontWeight.Bold)
)

val FeatherFontFamily = FontFamily(
    Font(R.font.feather_bold, FontWeight.Bold)
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
    labelMedium = baseline.labelMedium.copy(fontFamily = EpilogueFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = EpilogueFontFamily),
    // typography used in homepages
    bodySmall = TextStyle(
        fontFamily = BloggerSansFontFamily,
        fontWeight = FontWeight.Normal,
        color = Color(0xFF252244),
        fontSize = 14.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = BloggerSansFontFamily,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF252244),
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = BloggerSansFontFamily,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF252244),
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FeatherFontFamily,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF252244),
        fontSize = 16.sp
    ),
)