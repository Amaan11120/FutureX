package com.amaan.futurex

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RemindersScreen() {
    val ctx = LocalContext.current
    var list by remember { mutableStateOf(RemindersStore.load(ctx)) }
    var title by remember { mutableStateOf("") }
    var mins by remember { mutableStateOf<Int?>(null) }
    fun fmt(m: Int) = String.format("%02d:%02d", m / 60, m % 60)
    fun change(new: List<Reminder>) {
        list = new
        RemindersStore.save(ctx, new)
    }
    fun toggle(r: Reminder, on: Boolean) {
        change(list.map { if (it.id == r.id) it.copy(on = on) else it })
        if (on) CustomReminder.enable(ctx, r) else CustomReminder.disable(ctx, r.id)
    }
    fun remove(r: Reminder) {
        CustomReminder.disable(ctx, r.id)
        change(list.filter { it.id != r.id })
    }
    Column(Modifier.fillMaxSize().fxGradient().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Reminders", color = Color.White, fontSize = 24.sp)
        Text("Daily reminders at a time you choose", color = FxMuted, fontSize = 12.sp)
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Reminder text") },
            singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedButton(onClick = {
            TimePickerDialog(ctx, { _, h, m -> mins = h * 60 + m }, 9, 0, true).show()
        }, modifier = Modifier.fillMaxWidth()) { Text(mins?.let { fmt(it) } ?: "Choose time") }
        Button(onClick = {
            val r = Reminder(System.currentTimeMillis(), title.trim(), mins ?: 0, true)
            change(list + r)
            CustomReminder.enable(ctx, r)
            title = ""; mins = null
        }, enabled = title.isNotBlank() && mins != null, modifier = Modifier.fillMaxWidth()) { Text("Add reminder") }
        if (list.isEmpty()) Text("No reminders yet.", color = FxMuted)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(list, key = { it.id }) { r ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = r.on, onCheckedChange = { toggle(r, it) })
                    Text("  " + fmt(r.minutes) + "  " + r.title, color = Color.White, modifier = Modifier.weight(1f))
                    TextButton(onClick = { remove(r) }) { Text("Delete", color = FxPurple) }
                }
            }
        }
    }
}
