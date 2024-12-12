package com.amarchaud.templateresume.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private val primary = Color(0xFF1565C0)
private val secondary = Color(0xFF2196F3)
private val tertiary = Color(0xFFE3F2FD)

private val background = Color(0xFFE3F2FD)
private val white = Color.White

private val LightColorScheme = lightColorScheme(
    primary = primary,
    secondary = secondary,
    tertiary = tertiary,
    onPrimary = white,
    onSecondary = white,
    onTertiary = white,
    background = background,
)

@Composable
fun TemplateResumeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = StoreDynamicColor.getInstance().dynamicColor.collectAsState().value,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> LightColorScheme // fallback to light theme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

class StoreDynamicColor private constructor() {
    companion object {

        @Volatile
        private var instance: StoreDynamicColor? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: StoreDynamicColor().also { instance = it }
            }
    }

    val canBeDisplayed = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    // Dynamic color is available on Android 12+
    private val _dynamicColor = MutableStateFlow(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
    val dynamicColor = _dynamicColor.asStateFlow()

    fun activateDynamicColor(b: Boolean) {
        _dynamicColor.update { b }
    }
}