package com.amaan.futurex

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaan.futurex.data.AppDatabase
import java.time.LocalDate
import java.util.Locale

@Composable
private fun AStat(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier.background(FxSurface, RoundedCornerShape(16.dp)).padding(14.dp)
    ) {
        Text(value, color = Color.White, fontSize = 24.sp)
        Text(label, color = FxMuted, fontSize = 12.sp)
    }
}

@Composable
fun AnalyticsScreen(taskVm: TaskViewModel) {
    val ctx = LocalContext.current
    val sessions by remember { AppDatabase.get(ctx).focusSessionDao().all() }.collectAsState(emptyList())
    val tasks by taskVm.tasks.collectAsState()
    val today = LocalDate.now().toEpochDay()
    val s = StatsCalc.compute(sessions, tasks, today, Prefs.goalHours(ctx))

    Column(
        Modifier.fillMaxSize().fxGradient().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Analytics", color = Color.White, fontSize = 28.sp)
        Text(s.summary, color = FxMuted, fontSize = 14.sp)
        if (!s.hasData) {
            Text(
                "Nothing to chart yet. Complete a focus round or tick a task and your stats will appear here.",
                color = FxMuted, fontSize = 14.sp
            )
            return@Column
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AStat("Current streak (days)", s.currentStreak.toString(), Modifier.weight(1f))
            AStat("Longest streak (days)", s.longestStreak.toString(), Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AStat("Studied today (min)", s.todayMin.toString(), Modifier.weight(1f))
            AStat("Daily goal", "${s.goalPct}%", Modifier.weight(1f))
        }
        AStat("Tasks completed", "${s.tasksDone} / ${s.tasksTotal}", Modifier.fillMaxWidth())

        Text("Last 7 days (minutes)", color = Color.White, fontSize = 16.sp)
        val max = (s.last7.maxOrNull() ?: 0).coerceAtLeast(1)
        val desc = s.last7.joinToString(", ") { "$it minutes" }
        Row(
            Modifier.fillMaxWidth().semantics { contentDescription = "Study minutes for the last 7 days: $desc" },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            s.last7.forEachIndexed { i, m ->
                val day = LocalDate.ofEpochDay(today - (6 - i))
                val label = day.dayOfWeek.getDisplayName(java.time.format.TextStyle.NARROW, Locale.getDefault())
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (m > 0) m.toString() else "", color = FxMuted, fontSize = 10.sp)
                    Box(
                        Modifier.fillMaxWidth().height((m * 100 / max).coerceAtLeast(2).dp)
                            .background(if (m > 0) FxPurple else FxSurface, RoundedCornerShape(6.dp))
                    )
                    Text(label, color = FxMuted, fontSize = 11.sp)
                }
            }
        }

        if (s.bySubject.isNotEmpty()) {
            Text("By subject", color = Color.White, fontSize = 16.sp)
            s.bySubject.forEach { (name, min) ->
                Text("$name: $min min", color = FxMuted, fontSize = 14.sp)
            }
        } else {
            Text("Subject time appears once focus sessions record a subject.", color = FxMuted, fontSize = 12.sp)
        }
    }
}
