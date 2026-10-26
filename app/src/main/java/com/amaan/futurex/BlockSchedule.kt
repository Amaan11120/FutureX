package com.amaan.futurex

import android.content.Context
import java.time.LocalTime

object BlockSchedule {
    fun active(c: Context): Boolean {
        if (!Prefs.schedOn(c)) return true
        val now = LocalTime.now().let { it.hour * 60 + it.minute }
        val s = Prefs.schedStart(c)
        val e = Prefs.schedEnd(c)
        return if (s <= e) now in s until e else now >= s || now < e
    }
}
