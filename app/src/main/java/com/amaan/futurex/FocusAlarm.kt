package com.amaan.futurex

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

object FocusAlarm {
    private fun pi(ctx: Context): PendingIntent = PendingIntent.getBroadcast(
        ctx, 7001, Intent(ctx, FocusAlarmReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    fun sync(ctx: Context, on: Boolean, endAt: Long) {
        val am = ctx.getSystemService(AlarmManager::class.java)
        if (on && endAt > System.currentTimeMillis()) {
            val show = PendingIntent.getActivity(
                ctx, 7002, Intent(ctx, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            am.setAlarmClock(AlarmManager.AlarmClockInfo(endAt, show), pi(ctx))
        } else {
            am.cancel(pi(ctx))
        }
    }
}

class FocusAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pending = goAsync()
        val app = context.applicationContext
        Thread {
            try {
                val sp = app.getSharedPreferences("fx_focus", Context.MODE_PRIVATE)
                if (sp.getInt("state", 0) == 1) {
                    val isBreak = sp.getBoolean("is_break", false)
                    kotlinx.coroutines.runBlocking { FocusSettle.run(app) }
                    val nm = app.getSystemService(NotificationManager::class.java)
                    if (Build.VERSION.SDK_INT >= 26) {
                        nm.createNotificationChannel(
                            NotificationChannel("focus", "Focus timer", NotificationManager.IMPORTANCE_HIGH)
                        )
                    }
                    val open = PendingIntent.getActivity(
                        app, 7003, Intent(app, MainActivity::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    val n = NotificationCompat.Builder(app, "focus")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(if (isBreak) "Break over" else "Focus round complete")
                        .setContentText(if (isBreak) "Ready for the next round?" else "Nice work. Take a break.")
                        .setContentIntent(open)
                        .setAutoCancel(true)
                        .build()
                    nm.notify(7004, n)
                }
            } finally {
                pending.finish()
            }
        }.start()
    }
}
