package com.amaan.futurex

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun BlockScheduleCard() {
    val ctx = LocalContext.current
    var on by remember { mutableStateOf(Prefs.schedOn(ctx)) }
    var s by remember { mutableStateOf(Prefs.schedStart(ctx)) }
    var e by remember { mutableStateOf(Prefs.schedEnd(ctx)) }
    fun save() = Prefs.setSched(ctx, on, s, e)
    fun fmt(m: Int) = String.format("%02d:%02d", m / 60, m % 60)
    Column {
        Text("Only block during a schedule", color = Color.White)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = on, onCheckedChange = { on = it; save() })
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = {
                TimePickerDialog(ctx, { _, h, m -> s = h * 60 + m; save() }, s / 60, s % 60, true).show()
            }) { Text(fmt(s)) }
            Text("  to  ", color = Color.White)
            OutlinedButton(onClick = {
                TimePickerDialog(ctx, { _, h, m -> e = h * 60 + m; save() }, e / 60, e % 60, true).show()
            }) { Text(fmt(e)) }
        }
    }
}
