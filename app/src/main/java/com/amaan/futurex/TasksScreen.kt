package com.amaan.futurex

import androidx.compose.foundation.background
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
import android.app.DatePickerDialog
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate

@Composable
fun TasksScreen(vm: TaskViewModel) {
    val tasks by vm.tasks.collectAsState()
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var picked by remember { mutableStateOf<LocalDate?>(null) }
    val context = LocalContext.current
    Column(
        Modifier.fillMaxSize().fxGradient()
            .padding(horizontal = 18.dp, vertical = 24.dp)
    ) {
        Text("FutureX", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
        Text("Made by Amaan", color = FxPurple, fontSize = 12.sp)
        OutlinedTextField(
            value = title, onValueChange = { title = it },
            label = { Text("New study task") }, singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(top = 18.dp)
        )
        OutlinedTextField(
            value = subject, onValueChange = { subject = it },
            label = { Text("Subject") }, singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        OutlinedButton(
            onClick = {
                val t = LocalDate.now()
                DatePickerDialog(context, { _, y, m, d -> picked = LocalDate.of(y, m + 1, d) }, t.year, t.monthValue - 1, t.dayOfMonth).show()
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) { Text(picked?.toString() ?: "Due date (optional)") }
        Button(
            onClick = { vm.add(title, subject, picked?.toEpochDay()); title = ""; subject = ""; picked = null },
            enabled = title.isNotBlank(),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) { Text("Add task") }
        if (tasks.isEmpty()) {
            Text(
                "No tasks yet. Add your first one above.",
                color = FxMuted,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        LazyColumn(
            Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks, key = { it.id }) { TaskRow(it, vm) }
        }
    }
}
