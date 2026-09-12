package com.markvoronin.mapspowersaving.services.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.markvoronin.mapspowersaving.domain.MapsState
import com.markvoronin.mapspowersaving.shizuku.ShizukuProcessHelper

class SecurityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_OFF) {
            Log.d("MapsPowerSaving", "Screen OFF. isEnabled: ${MapsState.isEnabled}, hasNav: ${MapsState.hasNavigationNotification}")
            if (MapsState.isEnabled && MapsState.hasNavigationNotification) {
                Log.d("MapsPowerSaving", "Triggering Maps MinMode.")
                try {
                    val process = ShizukuProcessHelper.newProcess(
                        arrayOf("am", "start", "-n", "com.google.android.apps.maps/com.google.android.apps.gmm.features.minmode.MinModeActivity")
                    )
                    if (process == null) {
                        Log.e("MapsPowerSaving", "Failed to start Shizuku process (process is null)")
                    }
                } catch (e: Exception) {
                    Log.e("MapsPowerSaving", "Failed to trigger MinMode via Shizuku", e)
                }
            }
        }
    }
}
