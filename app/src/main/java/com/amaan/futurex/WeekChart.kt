package com.amaan.futurex

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate

@Composable
fun WeekChart(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    val today = LocalDate.now().toEpochDay()
    val days = (6 downTo 0).map { today - it }
    val mins = days.map { Prefs.focusMinutes(ctx, it) }
    val top = (mins.maxOrNull() ?: 0).coerceAtLeast(1)
    Row(
        modifier.height(120.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        days.forEachIndexed { i, d ->
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.fillMaxWidth().height((80 * mins[i] / top).dp).background(FxPurple))
                Text(LocalDate.ofEpochDay(d).dayOfWeek.name.take(1), color = FxMuted, fontSize = 10.sp)
            }
        }
    }
}
