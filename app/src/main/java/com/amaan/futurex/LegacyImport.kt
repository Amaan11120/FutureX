package com.amaan.futurex

import android.content.Context
import com.amaan.futurex.data.AppDatabase
import com.amaan.futurex.data.FocusSession

object LegacyImport {
    suspend fun run(ctx: Context) {
        val flags = ctx.getSharedPreferences("fx_focus", Context.MODE_PRIVATE)
        if (flags.getBoolean("legacy_done", false)) return
        val old = ctx.getSharedPreferences("futurex_prefs", Context.MODE_PRIVATE)
        val dao = AppDatabase.get(ctx).focusSessionDao()
        for ((k, v) in old.all) {
            if (!k.startsWith("focus_") || v !is Int || v <= 0) continue
            val day = k.removePrefix("focus_").toLongOrNull() ?: continue
            val extra = v - dao.realMinutes(day)
            if (extra > 0) dao.insert(FocusSession("legacy-$day", day, day * 86_400_000L, extra))
        }
        flags.edit().putBoolean("legacy_done", true).apply()
    }
}
