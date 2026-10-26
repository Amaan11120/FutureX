package com.amaan.futurex

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaan.futurex.data.AppDatabase
import com.amaan.futurex.data.FocusSession
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId
import java.util.UUID
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

private const val IDLE = 0
private const val RUNNING = 1
private const val PAUSED = 2

@Composable
fun FocusScreen() {
    val ctx = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val sp = remember { ctx.getSharedPreferences("fx_focus", Context.MODE_PRIVATE) }
    var focusMin by remember { mutableStateOf(sp.getInt("focus_min", 25)) }
    var breakMin by remember { mutableStateOf(sp.getInt("break_min", 5)) }
    var isBreak by remember { mutableStateOf(sp.getBoolean("is_break", false)) }
    var state by remember { mutableStateOf(sp.getInt("state", IDLE)) }
    var endAt by remember { mutableStateOf(sp.getLong("end_at", 0L)) }
    var leftMs by remember {
        mutableStateOf(sp.getLong("left_ms", (if (isBreak) breakMin else focusMin) * 60_000L))
    }
    var sid by remember { mutableStateOf(sp.getString("sid", null)) }
    var startedAt by remember { mutableStateOf(sp.getLong("started", 0L)) }
    var now by remember { mutableStateOf(System.currentTimeMillis()) }
    var focusText by remember { mutableStateOf(focusMin.toString()) }
    var breakText by remember { mutableStateOf(breakMin.toString()) }

    fun total() = (if (isBreak) breakMin else focusMin) * 60_000L

    fun save() {
        FocusAlarm.sync(ctx, state == RUNNING, endAt)
        sp.edit().putInt("focus_min", focusMin).putInt("break_min", breakMin)
            .putBoolean("is_break", isBreak).putInt("state", state)
            .putLong("end_at", endAt).putLong("left_ms", leftMs)
            .putString("sid", sid).putLong("started", startedAt).apply()
    }

    suspend fun finish() {
        if (!isBreak) {
            val id = sid
            if (id != null) {
                val day = Instant.ofEpochMilli(endAt).atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay()
                val row = AppDatabase.get(ctx).focusSessionDao().insert(FocusSession(id, day, startedAt, focusMin))
                if (row != -1L) Prefs.addFocusMinutes(ctx, day, focusMin)
            }
        }
        sid = null
        state = IDLE
        leftMs = total()
        save()
    }

    LaunchedEffect(state, endAt) {
        while (state == RUNNING) {
            now = System.currentTimeMillis()
            if (now >= endAt) { finish(); break }
            delay(500)
        }
    }

    val remaining = if (state == RUNNING) (endAt - now).coerceIn(0L, total()) else leftMs
    val sec = (remaining + 999) / 1000

    Column(
        Modifier.fillMaxSize().fxGradient().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (isBreak) "Break" else "Focus", color = FxMuted, fontSize = 16.sp)
        Text(String.format("%02d:%02d", sec / 60, sec % 60), color = Color.White, fontSize = 64.sp, modifier = Modifier.movingGlow())
        Spacer(Modifier.height(12.dp))
        LinearProgressIndicator(progress = 1f - (remaining.toFloat() / total().toFloat()).coerceIn(0f, 1f), modifier = Modifier.fillMaxWidth(0.7f).height(6.dp), color = FxPurple, trackColor = FxSurface)
        Row(Modifier.padding(top = 24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (state == RUNNING) {
                        leftMs = (endAt - System.currentTimeMillis()).coerceIn(0L, total())
                        state = PAUSED
                    } else if (leftMs > 0) {
                        val t = System.currentTimeMillis()
                        if (!isBreak && sid == null) { sid = UUID.randomUUID().toString(); startedAt = t }
                        endAt = t + leftMs
                        now = t
                        state = RUNNING
                    }
                    save()
                },
                enabled = leftMs > 0 || state == RUNNING
            ) { Text(if (state == RUNNING) "Pause" else "Start") }
            OutlinedButton(onClick = {
                state = IDLE; sid = null; leftMs = total(); save()
            }) { Text("Reset") }
        }
        TextButton(onClick = {
            isBreak = !isBreak; state = IDLE; sid = null; leftMs = total(); save()
        }) { Text(if (isBreak) "Switch to Focus" else "Switch to Break") }

        Spacer(Modifier.height(20.dp))
        if (state == IDLE) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = focusText, onValueChange = { focusText = it.filter(Char::isDigit).take(3) },
                    label = { Text("Focus (min)") }, singleLine = true, modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = breakText, onValueChange = { breakText = it.filter(Char::isDigit).take(2) },
                    label = { Text("Break (min)") }, singleLine = true, modifier = Modifier.weight(1f)
                )
            }
            val f = focusText.toIntOrNull()
            val b = breakText.toIntOrNull()
            val ok = f != null && f in 1..180 && b != null && b in 1..60
            if (!ok) Text("Focus 1-180 min, break 1-60 min", color = FxPurple, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { focusMin = f!!; breakMin = b!!; leftMs = total(); save() },
                enabled = ok, modifier = Modifier.fillMaxWidth()
            ) { Text("Set timer") }
        } else {
            Text("Reset the timer to change the lengths.", color = FxMuted, fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}
