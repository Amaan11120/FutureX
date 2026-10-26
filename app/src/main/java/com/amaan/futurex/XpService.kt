package com.amaan.futurex

import android.content.Context
import com.amaan.futurex.data.AppDatabase
import com.amaan.futurex.data.Achievement
import com.amaan.futurex.data.XpEvent
import kotlinx.coroutines.flow.first
import java.time.LocalDate

object XpService {
    suspend fun task(ctx: Context, taskId: Long) {
        AppDatabase.get(ctx).xpDao().insert(XpEvent("task-$taskId", XpLogic.TASK_XP, System.currentTimeMillis()))
        check(ctx)
    }

    suspend fun focus(ctx: Context, sessionId: String, minutes: Int) {
        val xp = XpLogic.focusXp(minutes)
        if (xp > 0) AppDatabase.get(ctx).xpDao().insert(XpEvent("focus-$sessionId", xp, System.currentTimeMillis()))
        check(ctx)
    }

    // Unlocks only what real records prove. Each id is stored once.
    suspend fun check(ctx: Context) {
        val db = AppDatabase.get(ctx)
        val sessions = db.focusSessionDao().all().first()
        val tasks = db.studyTaskDao().observeAll().first()
        val s = StatsCalc.compute(sessions, tasks, LocalDate.now().toEpochDay(), Prefs.goalHours(ctx))
        val now = System.currentTimeMillis()
        val earned = buildList {
            if (tasks.any { it.isDone }) add("first_task")
            if (sessions.any { !it.id.startsWith("legacy-") }) add("first_focus")
            if (s.longestStreak >= 7) add("streak7")
            if (s.goalPct >= 100) add("daily_goal")
        }
        earned.forEach { db.xpDao().unlock(Achievement(it, now)) }
    }
}
