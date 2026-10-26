package com.amaan.futurex

import android.content.Context

object Prefs {
    private fun sp(c: Context) = c.getSharedPreferences("futurex_prefs", Context.MODE_PRIVATE)
    fun name(c: Context): String = sp(c).getString("name", "") ?: ""
    fun setName(c: Context, v: String) { sp(c).edit().putString("name", v.trim()).apply() }
    fun goalHours(c: Context): Int = sp(c).getInt("goal", 3)
    fun setGoalHours(c: Context, v: Int) { sp(c).edit().putInt("goal", v).apply() }
    private fun key(day: Long) = "focus_" + day
    fun focusMinutes(c: Context, day: Long): Int = sp(c).getInt(key(day), 0)
    fun addFocusMinutes(c: Context, day: Long, m: Int) {
        sp(c).edit().putInt(key(day), focusMinutes(c, day) + m).apply()
    }
    fun reminderOn(c: Context): Boolean = sp(c).getBoolean("rem_on", false)
    fun reminderHour(c: Context): Int = sp(c).getInt("rem_h", 18)
    fun reminderMinute(c: Context): Int = sp(c).getInt("rem_m", 0)
    fun setReminder(c: Context, on: Boolean, h: Int, m: Int) {
        sp(c).edit().putBoolean("rem_on", on).putInt("rem_h", h).putInt("rem_m", m).apply()
    }
    fun blockOn(c: Context): Boolean = sp(c).getBoolean("block_on", false)
    fun setBlockOn(c: Context, v: Boolean) { sp(c).edit().putBoolean("block_on", v).apply() }
    fun blocked(c: Context): Set<String> = sp(c).getStringSet("blocked", emptySet()) ?: emptySet()
    fun setBlocked(c: Context, s: Set<String>) { sp(c).edit().putStringSet("blocked", HashSet(s)).apply() }
    fun schedOn(c: Context): Boolean = sp(c).getBoolean("sch_on", false)
    fun schedStart(c: Context): Int = sp(c).getInt("sch_s", 9 * 60)
    fun schedEnd(c: Context): Int = sp(c).getInt("sch_e", 17 * 60)
    fun setSched(c: Context, on: Boolean, s: Int, e: Int) {
        sp(c).edit().putBoolean("sch_on", on).putInt("sch_s", s).putInt("sch_e", e).apply()
    }
}
