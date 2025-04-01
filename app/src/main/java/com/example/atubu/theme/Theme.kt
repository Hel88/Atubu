/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.atubu.theme

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF4CAF50), // Vert nature
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF1B5E20),
    secondary = Color(0xFF8BC34A), // Vert clair
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFFDCEDC8),
    onSecondaryContainer = Color(0xFF33691E),
    tertiary = Color(0xFF689F38),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFAED581),
    onTertiaryContainer = Color(0xFF1B5E20),
    error = Color(0xFFD32F2F),
    errorContainer = Color(0xFFFFCDD2),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFFB71C1C),
    background = Color(0xFFF1F8E9), // Vert très clair
    onBackground = Color(0xFF2E7D32),
    surface = Color(0xFFDCEDC8),
    onSurface = Color(0xFF1B5E20),
    surfaceVariant = Color(0xFFA5D6A7),
    onSurfaceVariant = Color(0xFF33691E),
    outline = Color(0xFF4CAF50),
    inverseOnSurface = Color(0xFFF1F8E9),
    inverseSurface = Color(0xFF2E7D32),
    inversePrimary = Color(0xFF8BC34A),
    surfaceTint = Color(0xFF4CAF50),
    outlineVariant = Color(0xFF66BB6A),
    scrim = Color(0xFF000000),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = Color(0xFFC8E6C9),
    secondary = Color(0xFF33691E),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF1B5E20),
    onSecondaryContainer = Color(0xFFAED581),
    tertiary = Color(0xFF8BC34A),
    onTertiary = Color(0xFF000000),
    tertiaryContainer = Color(0xFF4CAF50),
    onTertiaryContainer = Color(0xFFC8E6C9),
    error = Color(0xFFFF5252),
    errorContainer = Color(0xFFB71C1C),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFFFFCDD2),
    background = Color(0xFF1B5E20),
    onBackground = Color(0xFFC8E6C9),
    surface = Color(0xFF2E7D32),
    onSurface = Color(0xFFAED581),
    surfaceVariant = Color(0xFF4CAF50),
    onSurfaceVariant = Color(0xFFC8E6C9),
    outline = Color(0xFF81C784),
    inverseOnSurface = Color(0xFF1B5E20),
    inverseSurface = Color(0xFFC8E6C9),
    inversePrimary = Color(0xFF8BC34A),
    surfaceTint = Color(0xFF4CAF50),
    outlineVariant = Color(0xFF66BB6A),
    scrim = Color(0xFF000000),
)

@Composable
fun AtubuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+ but turned off for training purposes
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
