package org.citruscircuits.overrate.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * The app's theme.
 * Automatically follows the system theme and uses dynamic theming if available.
 *
 * @param content Content to show in this theme.
 */
@Composable
fun OverRateTheme(content: @Composable () -> Unit) {
    // check if dark theme should be used
    val useDarkTheme = isSystemInDarkTheme()
    // dynamic color is only in Android 12+
    val useDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    // choose the color scheme
    val colorScheme =
        if (useDynamicColor) {
            if (useDarkTheme) {
                dynamicDarkColorScheme(LocalContext.current)
            } else {
                dynamicLightColorScheme(LocalContext.current)
            }
        } else {
            if (useDarkTheme) darkColorScheme() else lightColorScheme()
        }
    // set system status bar colors based on the theme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            WindowCompat.getInsetsController((view.context as Activity).window, view).isAppearanceLightStatusBars =
                !useDarkTheme
        }
    }
    // use Material theming
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
