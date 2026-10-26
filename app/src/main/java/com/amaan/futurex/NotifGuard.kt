package com.amaan.futurex

import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationManagerCompat

fun hasNotifAccess(c: Context): Boolean =
    NotificationManagerCompat.getEnabledListenerPackages(c).contains(c.packageName)

class NotifGuard : NotificationListenerService() {
    private fun shouldHide(s: StatusBarNotification): Boolean =
        Prefs.blockOn(this) && s.packageName != packageName && s.packageName in Prefs.blocked(this)

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (shouldHide(sbn)) try { cancelNotification(sbn.key) } catch (_: Exception) {}
    }

    override fun onListenerConnected() {
        try {
            activeNotifications?.filter { shouldHide(it) }?.forEach { cancelNotification(it.key) }
        } catch (_: Exception) {}
    }
}
