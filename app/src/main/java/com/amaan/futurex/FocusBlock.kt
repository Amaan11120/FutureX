package com.amaan.futurex

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

object FocusBlock {
    private fun sp(c: Context) = c.getSharedPreferences("fx_focus", Context.MODE_PRIVATE)

    // Returns null on success (or if blocking was already on), else a message to show.
    fun start(c: Context): String? {
        if (Prefs.blockOn(c)) return null
        if (!hasUsageAccess(c) || !hasOverlay(c)) {
            return "Blocking couldn't turn on. Grant its permissions in More > Blocking."
        }
        Prefs.setBlockOn(c, true)
        ContextCompat.startForegroundService(c, Intent(c, BlockService::class.java))
        sp(c).edit().putBoolean("auto_block", true).apply()
        return null
    }

    fun stop(c: Context) {
        if (!sp(c).getBoolean("auto_block", false)) return
        Prefs.setBlockOn(c, false)
        c.stopService(Intent(c, BlockService::class.java))
        sp(c).edit().putBoolean("auto_block", false).apply()
    }
}
