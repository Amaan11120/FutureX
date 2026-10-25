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
}
