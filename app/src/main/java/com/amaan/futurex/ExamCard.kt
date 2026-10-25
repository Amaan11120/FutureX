package com.amaan.futurex

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amaan.futurex.data.Exam
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun ExamCard(exam: Exam, vm: ExamViewModel) {
    val date = LocalDate.ofEpochDay(exam.dateEpochDay)
    val days = ChronoUnit.DAYS.between(LocalDate.now(), date)
    val left = when {
        days > 1 -> "$days days left"
        days == 1L -> "Tomorrow"
        days == 0L -> "Today"
        else -> "Passed"
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = FxSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(exam.name, color = Color.White, fontWeight = FontWeight.Bold)
                Text("$date  -  $left", color = FxMuted)
            }
            TextButton({ vm.delete(exam) }) { Text("Delete", color = FxPurple) }
        }
    }
}
