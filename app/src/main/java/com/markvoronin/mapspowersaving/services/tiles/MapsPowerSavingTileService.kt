package com.markvoronin.mapspowersaving.services.tiles

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.markvoronin.mapspowersaving.R
import com.markvoronin.mapspowersaving.domain.MapsState

class MapsPowerSavingTileService : TileService() {

    private var prefs: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        prefs = applicationContext.getSharedPreferences("maps_power_saving_prefs", MODE_PRIVATE)
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        super.onClick()
        val isEnabled = prefs?.getBoolean("maps_power_saving_enabled", false) ?: false
        val newState = !isEnabled
        prefs?.edit()?.putBoolean("maps_power_saving_enabled", newState)?.apply()
        
        MapsState.isEnabled = newState
        updateTile()
    }

    private fun updateTile() {
        val tile = qsTile ?: return
        val isEnabled = prefs?.getBoolean("maps_power_saving_enabled", false) ?: false

        tile.state = if (isEnabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.label = getString(R.string.tile_maps_power_saving)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tile.subtitle = if (isEnabled) getString(R.string.tile_active) else getString(R.string.tile_inactive)
        }
        tile.updateTile()
    }
}
