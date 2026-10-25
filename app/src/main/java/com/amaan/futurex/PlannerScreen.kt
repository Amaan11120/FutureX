package com.amaan.futurex

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlannerScreen(taskVm: TaskViewModel) {
    val tasks by taskVm.tasks.collectAsState()
    var hours by remember { mutableStateOf("3") }
    var plan by remember { mutableStateOf<List<PlanItem>?>(null) }
    Column(
        Modifier.fillMaxSize().fxGradient().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Study Planner", color = Color.White, fontSize = 28.sp)
        Text("Offline planner: rule-based, no AI model", color = FxPurple, fontSize = 12.sp)
        OutlinedTextField(
            value = hours, onValueChange = { hours = it },
            label = { Text("Hours available today") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                val h = hours.toDoubleOrNull() ?: 0.0
                plan = buildPlan(tasks, (h * 60).toInt())
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Generate plan") }
        val p = plan
        if (p != null && p.isEmpty()) {
            Text("No pending tasks to plan, or 0 hours.", color = FxMuted)
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(p ?: emptyList()) {
                val t = "${it.minutes / 60}h ${it.minutes % 60}m, ${it.count} task(s)"
                StatCard(it.subject, t, Modifier.fillMaxWidth())
            }
        }
    }
}
