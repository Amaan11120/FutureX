package com.amaan.futurex

import android.content.Context

object Prefs {
    private fun sp(c: Context) = c.getSharedPreferences("futurex_prefs", Context.MODE_PRIVATE)
    fun name(c: Context): String = sp(c).getString("name", "") ?: ""
    fun setName(c: Context, v: String) { sp(c).edit().putString("name", v.trim()).apply() }
    fun goalHours(c: Context): Int = sp(c).getInt("goal", 3)
    fun setGoalHours(c: Context, v: Int) { sp(c).edit().putInt("goal", v).apply() }
}
