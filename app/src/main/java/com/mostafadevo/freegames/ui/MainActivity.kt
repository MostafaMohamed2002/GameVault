@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.mostafadevo.freegames.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mostafadevo.freegames.domain.model.ThemePreference
import com.mostafadevo.freegames.ui.screens.NavHostScreen
import com.mostafadevo.freegames.ui.screens.settings_screen.SettingsScreenViewModel
import com.mostafadevo.freegames.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            Handler(Looper.getMainLooper()).postDelayed({
                // Dismiss the splash screen after 1 second delay
            }, 1000)
        }
        enableEdgeToEdge()
        setContent {
            val settingsViewModel = hiltViewModel<SettingsScreenViewModel>()
            val themeState = settingsViewModel.themeState.collectAsStateWithLifecycle()
            val dynamicTheme = settingsViewModel.dynamicThemeState.collectAsStateWithLifecycle()
            AppTheme(
                darkTheme = when (themeState.value) {
                    ThemePreference.LIGHT -> false
                    ThemePreference.DARK -> true
                    ThemePreference.SYSTEM -> isSystemInDarkTheme()
                },
                dynamicColor = dynamicTheme.value
            ) {
                NavHostScreen()
            }
        }
    }
}
