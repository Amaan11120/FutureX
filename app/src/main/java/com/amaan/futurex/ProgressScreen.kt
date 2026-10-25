package com.amaan.futurex

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import androidx.compose.ui.platform.LocalContext

@Composable
fun ProgressScreen(taskVm: TaskViewModel) {
    val tasks by taskVm.tasks.collectAsState()
    val today = LocalDate.now().toEpochDay()
    val done = tasks.count { it.isDone }
    val overdue = tasks.count { !it.isDone && (it.dueEpochDay ?: Long.MAX_VALUE) < today }
    val pct = if (tasks.isEmpty()) 0f else done.toFloat() / tasks.size
    val bySubject = tasks.groupBy { it.subject.ifBlank { "General" } }
    Column(
        Modifier.fillMaxSize().fxGradient().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("My Progress", color = Color.White, fontSize = 28.sp)
        Text("Based on your tasks only", color = FxMuted, fontSize = 12.sp)
        LinearProgressIndicator(
            progress = { pct },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = FxPurple
        )
        Text((pct * 100).toInt().toString() + "% of tasks completed", color = FxMuted)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Completed", done.toString(), Modifier.weight(1f))
            StatCard("Pending", (tasks.size - done).toString(), Modifier.weight(1f))
        }
        StatCard("Overdue", overdue.toString(), Modifier.fillMaxWidth())
        StatCard("Focus today", Prefs.focusMinutes(LocalContext.current, today).toString() + " min", Modifier.fillMaxWidth())
        Text("Last 7 days (focus minutes)", color = FxMuted, fontSize = 12.sp)
        WeekChart(Modifier.fillMaxWidth())
        if (tasks.isEmpty()) Text("No tasks yet. Add some in Tasks.", color = FxMuted)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(bySubject.entries.toList()) { e ->
                val d = e.value.count { it.isDone }
                StatCard(e.key, d.toString() + " of " + e.value.size + " done", Modifier.fillMaxWidth())
            }
        }
    }
}
