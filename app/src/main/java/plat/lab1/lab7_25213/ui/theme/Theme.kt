package plat.lab1.lab7_25213.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PortalGreenDark,
    onPrimary = Color.White,
    secondary = SpacePurple,
    onSecondary = Color.White,
    background = OffWhite,
    surface = Color.White
)

private val DarkColors = darkColorScheme(
    primary = PortalGreen,
    onPrimary = SpaceDark,
    secondary = SpacePurple,
    onSecondary = Color.White,
    background = SpaceDark,
    surface = SpaceDark
)

@Composable
fun Lab7Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}