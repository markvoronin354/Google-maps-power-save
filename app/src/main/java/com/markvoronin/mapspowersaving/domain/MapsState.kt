package com.markvoronin.mapspowersaving.domain

import android.content.Context

object MapsState {
    var hasNavigationNotification = false
    var isEnabled = false

    fun init(context: Context) {
        val prefs = context.getSharedPreferences("maps_power_saving_prefs", Context.MODE_PRIVATE)
        isEnabled = prefs.getBoolean("maps_power_saving_enabled", false)
    }
}
