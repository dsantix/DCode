package com.dsanti.dcode.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocalPolice
import androidx.compose.material.icons.rounded.Policy
import androidx.lifecycle.ViewModel
import com.dsanti.dcode.R
import com.dsanti.dcode.Screen

class SettingsViewModel : ViewModel() {

    val settingsItemList = listOf(SettingsItem(Icons.Rounded.History, R.string.settings_history, Screen.History),
        SettingsItem(Icons.Rounded.LocalPolice, R.string.privacy_policy, Screen.PrivacyPolicy), SettingsItem(Icons.Rounded.Info, R.string.settings_about, Screen.About)
    )
}

