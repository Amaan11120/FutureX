package com.amaan.futurex

import com.amaan.futurex.data.FocusSession
import com.amaan.futurex.data.StudyTask

data class Stats(
    val currentStreak: Int,
    val longestStreak: Int,
    val todayMin: Int,
    val last7: List<Int>,
    val tasksDone: Int,
    val tasksTotal: Int,
    val goalPct: Int,
    val bySubject: List<Pair<String, Int>>,
    val summary: String,
    val hasData: Boolean
)

object StatsCalc {
    fun compute(sessions: List<FocusSession>, tasks: List<StudyTask>, today: Long, goalHours: Int): Stats {
        val perDay = sessions.groupBy { it.dayEpoch }.mapValues { e -> e.value.sumOf { it.minutes } }
        val days = perDay.filterValues { it > 0 }.keys

        // Current streak: consecutive study days ending today, or yesterday if today has none yet.
        var d = if (today in days) today else today - 1
        var current = 0
        while (d in days) { current++; d-- }

        // Longest streak over all history.
        var longest = 0
        var run = 0
        var prev = Long.MIN_VALUE
        for (x in days.sorted()) {
            run = if (x == prev + 1) run + 1 else 1
            if (run > longest) longest = run
            prev = x
        }

        val last7 = (6 downTo 0).map { perDay[today - it] ?: 0 }
        val todayMin = perDay[today] ?: 0
        val goalMin = (goalHours * 60).coerceAtLeast(1)
        val goalPct = (todayMin * 100 / goalMin).coerceAtMost(100)
        val done = tasks.count { it.isDone }
        val bySubject = sessions.filter { !it.subject.isNullOrBlank() }
            .groupBy { it.subject!! }
            .map { it.key to it.value.sumOf { s -> s.minutes } }
            .sortedByDescending { it.second }
        val weekMin = last7.sum()
        val weekDays = last7.count { it > 0 }
        val hasData = sessions.isNotEmpty() || tasks.isNotEmpty()
        val summary = when {
            !hasData -> "No study records yet. Finish a focus round or a task to start your stats."
            weekMin == 0 -> "No focus time in the last 7 days. $done of ${tasks.size} tasks done."
            else -> "You studied $weekMin min across $weekDays of the last 7 days. $done of ${tasks.size} tasks done."
        }
        return Stats(current, longest, todayMin, last7, done, tasks.size, goalPct, bySubject, summary, hasData)
    }
}
