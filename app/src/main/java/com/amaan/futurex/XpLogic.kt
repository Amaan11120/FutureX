package com.amaan.futurex

import kotlin.math.sqrt

interface XpStore {
    // Returns true only if this event id was new.
    fun insertIfAbsent(id: String, xp: Int): Boolean
}

object XpLogic {
    const val TASK_XP = 10
    fun focusXp(minutes: Int) = minutes.coerceIn(0, 180)
    fun level(xp: Int): Int = sqrt(xp.coerceAtLeast(0) / 50.0).toInt() + 1
    fun levelStart(level: Int): Int = 50 * (level - 1) * (level - 1)
    fun progress(xp: Int): Float {
        val l = level(xp)
        val a = levelStart(l)
        val b = levelStart(l + 1)
        return ((xp - a).toFloat() / (b - a)).coerceIn(0f, 1f)
    }
}

class XpAwarder(private val store: XpStore) {
    fun awardTask(taskId: Long): Boolean = store.insertIfAbsent("task-$taskId", XpLogic.TASK_XP)
    fun awardFocus(sessionId: String, minutes: Int): Boolean {
        val xp = XpLogic.focusXp(minutes)
        return xp > 0 && store.insertIfAbsent("focus-$sessionId", xp)
    }
}
