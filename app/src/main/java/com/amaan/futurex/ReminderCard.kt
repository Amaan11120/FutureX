package com.amaan.futurex

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReminderCard() {
    val ctx = LocalContext.current
    var on by remember { mutableStateOf(Prefs.reminderOn(ctx)) }
    var h by remember { mutableStateOf(Prefs.reminderHour(ctx)) }
    var m by remember { mutableStateOf(Prefs.reminderMinute(ctx)) }
    fun apply(newOn: Boolean) {
        on = newOn
        Prefs.setReminder(ctx, on, h, m)
        if (on) StudyReminder.enable(ctx, h, m) else StudyReminder.disable(ctx)
    }
    Column {
        Text("Daily study reminder", color = FxMuted, fontSize = 12.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = on, onCheckedChange = { apply(it) })
            Spacer(Modifier.width(12.dp))
            OutlinedButton(onClick = {
                TimePickerDialog(ctx, { _, hh, mm ->
                    h = hh; m = mm
                    apply(on)
                }, h, m, true).show()
            }) { Text(String.format("%02d:%02d", h, m)) }
        }
    }
}
