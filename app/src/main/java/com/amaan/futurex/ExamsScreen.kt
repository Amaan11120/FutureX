package com.amaan.futurex

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaan.futurex.data.Exam
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun ExamsScreen(vm: ExamViewModel) {
    val exams by vm.exams.collectAsState()
    var name by remember { mutableStateOf("") }
    var picked by remember { mutableStateOf<LocalDate?>(null) }
    val context = LocalContext.current
    Column(
        Modifier.fillMaxSize().fxGradient()
            .padding(horizontal = 18.dp, vertical = 24.dp)
    ) {
        Text("Exams", color = Color.White, fontSize = 28.sp)
        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Exam name") }, singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )
        OutlinedButton(
            onClick = {
                val t = LocalDate.now()
                DatePickerDialog(
                    context,
                    { _, y, m, d -> picked = LocalDate.of(y, m + 1, d) },
                    t.year, t.monthValue - 1, t.dayOfMonth
                ).show()
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) { Text(picked?.toString() ?: "Choose exam date") }
        Button(
            onClick = {
                vm.add(name, picked!!.toEpochDay())
                name = ""; picked = null
            },
            enabled = name.isNotBlank() && picked != null,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) { Text("Add exam") }
        if (exams.isEmpty()) {
            Text("No exams yet.", color = FxMuted,
                modifier = Modifier.padding(top = 20.dp))
        }
        LazyColumn(Modifier.padding(top = 12.dp)) {
            items(exams, key = { it.id }) { ExamCard(it, vm) }
        }
    }
}
