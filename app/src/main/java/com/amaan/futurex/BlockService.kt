package com.amaan.futurex

import android.app.*
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class BlockService : Service() {
    @Volatile private var running = false
    private var last = ""

    override fun onBind(i: Intent?): IBinder? = null

    override fun onStartCommand(i: Intent?, f: Int, id: Int): Int {
        val nm = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= 26) {
            nm.createNotificationChannel(
                NotificationChannel("block", "App blocking", NotificationManager.IMPORTANCE_LOW)
            )
        }
        val n = NotificationCompat.Builder(this, "block")
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .setContentTitle("FutureX app blocking is on")
            .setContentText("Blocked apps are covered while this is on.")
            .build()
        startForeground(7001, n)
        if (!running) { running = true; Thread { loop() }.start() }
        return START_STICKY
    }

    private fun loop() {
        val usm = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        while (running) {
            val now = System.currentTimeMillis()
            val ev = usm.queryEvents(now - 5000, now)
            val e = UsageEvents.Event()
            while (ev.hasNextEvent()) {
                ev.getNextEvent(e)
                if (e.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) last = e.packageName
            }
            if (last in Prefs.blocked(this)) openBlock()
            Thread.sleep(1000)
        }
    }

    private fun openBlock() {
        last = packageName
        val i = Intent(this, BlockActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

    override fun onDestroy() {
        running = false
        super.onDestroy()
    }
}
