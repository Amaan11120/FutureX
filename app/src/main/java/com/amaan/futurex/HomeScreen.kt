package com.amaan.futurex

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import java.time.LocalDate
import androidx.compose.ui.platform.LocalContext

@Composable
fun HomeScreen(taskVm: TaskViewModel, examVm: ExamViewModel) {
    val tasks by taskVm.tasks.collectAsState()
    val exams by examVm.exams.collectAsState()
    val today = LocalDate.now().toEpochDay()
    val pending = tasks.count { !it.isDone }
    val upcoming = exams.filter { it.dateEpochDay >= today }
    val next = upcoming.firstOrNull()
    val ctx = LocalContext.current
    val who = Prefs.name(ctx)
    val goal = Prefs.goalHours(ctx)
    Column(
        Modifier.fillMaxSize().fxGradient().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("FutureX", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
        Text("Made by Amaan", color = FxPurple, fontSize = 12.sp)
        Text(if (who.isBlank()) "Welcome" else "Welcome, " + who, color = Color.White, fontSize = 18.sp)
        StatCard("Daily study goal", goal.toString() + " hours", Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Pending tasks", pending.toString(), Modifier.weight(1f))
            StatCard("Upcoming exams", upcoming.size.toString(), Modifier.weight(1f))
        }
        if (next == null) {
            StatCard("Next exam", "None yet", Modifier.fillMaxWidth())
        } else {
            val d = next.dateEpochDay - today
            val label = if (d == 0L) "Today" else "$d days"
            StatCard("Next: " + next.name, label, Modifier.fillMaxWidth())
        }
    }
}
