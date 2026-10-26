package com.amaan.futurex

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class Reminder(val id: Long, val title: String, val minutes: Int, val on: Boolean)

object RemindersStore {
    private fun sp(c: Context) = c.getSharedPreferences("futurex_reminders", Context.MODE_PRIVATE)

    fun load(c: Context): List<Reminder> {
        val a = JSONArray(sp(c).getString("list", "[]") ?: "[]")
        return (0 until a.length()).map {
            val o = a.getJSONObject(it)
            Reminder(o.getLong("id"), o.getString("title"), o.getInt("min"), o.getBoolean("on"))
        }
    }

    fun save(c: Context, l: List<Reminder>) {
        val a = JSONArray()
        for (r in l) a.put(JSONObject().put("id", r.id).put("title", r.title).put("min", r.minutes).put("on", r.on))
        sp(c).edit().putString("list", a.toString()).apply()
    }
}
