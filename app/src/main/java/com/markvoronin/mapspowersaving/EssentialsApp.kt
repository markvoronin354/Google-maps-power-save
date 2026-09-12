package com.markvoronin.mapspowersaving

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import com.markvoronin.mapspowersaving.domain.MapsState
import com.markvoronin.mapspowersaving.services.receivers.SecurityReceiver

class EssentialsApp : Application() {

    private val securityReceiver = SecurityReceiver()

    override fun onCreate() {
        super.onCreate()
        MapsState.init(this)
        
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(securityReceiver, filter)
    }

}
