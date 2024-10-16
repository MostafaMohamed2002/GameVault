package com.mostafadevo.freegames.ui.screens.settings_screen

import com.mostafadevo.freegames.domain.model.ThemePreference

sealed class SettingsScreenEvent {
    data class SaveThemePreference(val themePreference: ThemePreference) : SettingsScreenEvent()
}
