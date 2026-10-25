package com.amaan.futurex

import com.amaan.futurex.data.StudyTask
import java.time.LocalDate

data class PlanItem(val subject: String, val minutes: Int, val count: Int)

fun buildPlan(tasks: List<StudyTask>, totalMinutes: Int): List<PlanItem> {
    val today = LocalDate.now().toEpochDay()
    val pending = tasks.filter { !it.isDone }
    if (pending.isEmpty() || totalMinutes <= 0) return emptyList()
    fun weight(t: StudyTask): Int {
        val d = t.dueEpochDay ?: return 1
        return when {
            d - today <= 2 -> 3
            d - today <= 7 -> 2
            else -> 1
        }
    }
    val groups = pending.groupBy { it.subject.ifBlank { "General" } }
    val total = groups.values.sumOf { g -> g.sumOf { weight(it) } }
    return groups.map { (name, g) ->
        val w = g.sumOf { weight(it) }
        PlanItem(name, totalMinutes * w / total, g.size)
    }.sortedByDescending { it.minutes }
}
