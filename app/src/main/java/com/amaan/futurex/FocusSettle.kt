package com.amaan.futurex

import android.content.Context
import com.amaan.futurex.data.AppDatabase
import com.amaan.futurex.data.FocusSession
import java.time.Instant
import java.time.ZoneId

object FocusSettle {
    suspend fun run(ctx: Context) {
        val sp = ctx.getSharedPreferences("fx_focus", Context.MODE_PRIVATE)
        if (sp.getInt("state", 0) != 1) return
        val endAt = sp.getLong("end_at", 0L)
        if (System.currentTimeMillis() < endAt) return
        val isBreak = sp.getBoolean("is_break", false)
        val focusMin = sp.getInt("focus_min", 25)
        val breakMin = sp.getInt("break_min", 5)
        val id = sp.getString("sid", null)
        if (!isBreak && id != null) {
            val day = Instant.ofEpochMilli(endAt).atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay()
            val row = AppDatabase.get(ctx).focusSessionDao()
                .insert(FocusSession(id, day, sp.getLong("started", endAt), focusMin))
            if (row != -1L) Prefs.addFocusMinutes(ctx, day, focusMin)
        }
        FocusBlock.stop(ctx)
        sp.edit().putInt("state", 0).putString("sid", null)
            .putLong("left_ms", (if (isBreak) breakMin else focusMin) * 60_000L).apply()
    }
}
