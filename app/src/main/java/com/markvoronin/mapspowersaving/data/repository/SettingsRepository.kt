package com.markvoronin.mapspowersaving.data.repository

import android.content.Context
import android.content.SharedPreferences

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("maps_power_saving_prefs", Context.MODE_PRIVATE)

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    companion object {
        const val KEY_MAPS_POWER_SAVING_ENABLED = "maps_power_saving_enabled"
    }
}
