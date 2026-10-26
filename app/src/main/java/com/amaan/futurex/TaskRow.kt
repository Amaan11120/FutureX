package com.amaan.futurex

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaan.futurex.data.StudyTask
import java.time.LocalDate

@Composable
fun TaskRow(task: StudyTask, vm: TaskViewModel) {
    Card(
        colors = CardDefaults.cardColors(containerColor = FxSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(task.isDone, { vm.toggleDone(task) })
            Column(Modifier.weight(1f)) {
                Text(
                    task.title,
                    color = if (task.isDone) FxMuted else Color.White,
                    textDecoration = if (task.isDone) TextDecoration.LineThrough else null,
                    fontWeight = FontWeight.Bold
                )
                if (task.subject.isNotBlank()) {
                    Text(task.subject, color = FxMuted, fontSize = 12.sp)
                }
                task.dueEpochDay?.let { d ->
                    val late = d < LocalDate.now().toEpochDay() && !task.isDone
                    Text("Due " + LocalDate.ofEpochDay(d) + (task.dueMinutes?.let { String.format(" %02d:%02d", it / 60, it % 60) } ?: "") + if (late) " (overdue)" else "", color = if (late) FxPurple else FxMuted, fontSize = 12.sp)
                }
            }
            TextButton({ vm.delete(task) }) { Text("Delete", color = FxPurple) }
        }
    }
}
