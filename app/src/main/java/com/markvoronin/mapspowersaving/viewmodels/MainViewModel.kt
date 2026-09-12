package com.markvoronin.mapspowersaving.viewmodels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.markvoronin.mapspowersaving.shizuku.ShizukuPermissionHelper
import com.markvoronin.mapspowersaving.shizuku.ShizukuStatus
import com.markvoronin.mapspowersaving.domain.MapsState
import rikka.shizuku.Shizuku

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs: SharedPreferences = application.getSharedPreferences("maps_power_saving_prefs", Context.MODE_PRIVATE)
    private val shizukuHelper = ShizukuPermissionHelper(application)
    
    val isMapsPowerSavingEnabled = mutableStateOf(prefs.getBoolean("maps_power_saving_enabled", false))
    val isNotificationListenerEnabled = mutableStateOf(checkNotificationListenerPermission(application))
    val shizukuStatus = mutableStateOf(shizukuHelper.getStatus())
    
    private val shizukuListener = Shizuku.OnRequestPermissionResultListener { _, _ ->
        shizukuStatus.value = shizukuHelper.getStatus()
    }

    init {
        try {
            Shizuku.addRequestPermissionResultListener(shizukuListener)
        } catch (e: Exception) {}
        MapsState.isEnabled = isMapsPowerSavingEnabled.value
    }

    override fun onCleared() {
        try {
            Shizuku.removeRequestPermissionResultListener(shizukuListener)
        } catch (e: Exception) {}
    }

    fun refreshPermissions(context: Context) {
        isNotificationListenerEnabled.value = checkNotificationListenerPermission(context)
        shizukuStatus.value = shizukuHelper.getStatus()
        isMapsPowerSavingEnabled.value = MapsState.isEnabled
    }

    fun setMapsPowerSavingEnabled(enabled: Boolean, context: Context) {
        isMapsPowerSavingEnabled.value = enabled
        MapsState.isEnabled = enabled
        prefs.edit().putBoolean("maps_power_saving_enabled", enabled).apply()
    }

    private fun checkNotificationListenerPermission(context: Context): Boolean {
        val packageName = context.packageName
        val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return flat?.contains(packageName) == true
    }
    
    fun requestNotificationListenerPermission(context: Context) {
        context.startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
    
    fun requestShizukuPermission() {
        try {
            if (Shizuku.pingBinder()) {
                Shizuku.requestPermission(0)
            } else {
                // Not running
                shizukuStatus.value = ShizukuStatus.NOT_RUNNING
            }
        } catch (e: Exception) {}
    }

}
