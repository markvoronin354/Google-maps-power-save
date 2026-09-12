package com.markvoronin.mapspowersaving.services

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.markvoronin.mapspowersaving.domain.MapsState

class NotificationListener : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        checkActiveNotifications()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName == "com.google.android.apps.maps") {
            val isNav = isNavigationNotification(sbn)
            Log.d("MapsPowerSaving", "Posted: ${sbn.packageName}, Channel: ${sbn.notification.channelId}, isNav: $isNav")
            if (isNav) {
                MapsState.hasNavigationNotification = true
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        if (sbn.packageName == "com.google.android.apps.maps") {
            Log.d("MapsPowerSaving", "Removed: ${sbn.packageName}")
            MapsState.hasNavigationNotification = false
        }
    }

    private fun checkActiveNotifications() {
        try {
            activeNotifications?.forEach { sbn ->
                if (sbn.packageName == "com.google.android.apps.maps" && isNavigationNotification(sbn)) {
                    MapsState.hasNavigationNotification = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isNavigationNotification(sbn: StatusBarNotification): Boolean {
        val notification = sbn.notification
        val channelId = notification.channelId
        val category = notification.category
        val isOngoing = (notification.flags and Notification.FLAG_ONGOING_EVENT) != 0
        
        // Comprehensive check for Maps navigation channels
        val knownChannels = setOf(
            "navigation_notification_channel",
            "primary_navigation_channel_v1",
            "primary_navigation_channel_v2"
        )
        
        val hasNavChannel = channelId != null && (knownChannels.contains(channelId) || channelId.contains("navigation", ignoreCase = true))
        val hasNavCategory = category == Notification.CATEGORY_NAVIGATION || category?.contains("navigation", ignoreCase = true) == true
        
        return isOngoing && (hasNavChannel || hasNavCategory)
    }
}
