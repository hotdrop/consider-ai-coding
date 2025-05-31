package jp.hotdrop.considercline.android.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ConsiderClineTheme(
    darkTheme: Boolean = false, //isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors(
            primary = Color(0xFF906c56),
        )
    } else {
        lightColors(
            primary = Color(0xFF906c56), // AppBarやButtonの背景色
            onPrimary = Color.White, // AppBarやButtonのテキスト色
        )
    }
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )
    MaterialTheme(
        colors = colors,
        shapes = shapes,
        content = content
    )
}