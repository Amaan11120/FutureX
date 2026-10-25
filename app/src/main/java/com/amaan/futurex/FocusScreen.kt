package com.amaan.futurex

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate

@Composable
fun FocusScreen() {
    val ctx = LocalContext.current
    var isBreak by remember { mutableStateOf(false) }
    var left by remember { mutableStateOf(25 * 60) }
    var running by remember { mutableStateOf(false) }
    LaunchedEffect(running, left) {
        if (running && left > 0) {
            delay(1000)
            left -= 1
        } else if (left == 0 && running) {
            if (!isBreak) Prefs.addFocusMinutes(ctx, LocalDate.now().toEpochDay(), 25)
            running = false
        }
    }
    Column(
        Modifier.fillMaxSize().fxGradient().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (isBreak) "Break" else "Focus", color = FxMuted, fontSize = 16.sp)
        Text(
            String.format("%02d:%02d", left / 60, left % 60),
            color = Color.White, fontSize = 64.sp
        )
        Row(Modifier.padding(top = 24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { running = !running }, enabled = left > 0) {
                Text(if (running) "Pause" else "Start")
            }
            OutlinedButton(onClick = {
                running = false
                left = if (isBreak) 5 * 60 else 25 * 60
            }) { Text("Reset") }
        }
        TextButton(onClick = {
            isBreak = !isBreak
            running = false
            left = if (isBreak) 5 * 60 else 25 * 60
        }) { Text(if (isBreak) "Switch to Focus" else "Switch to Break") }
    }
}
