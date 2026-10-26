package com.amaan.futurex

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val presets = listOf(
    "Violet" to 0xFFB66CFF.toInt(), "Blue" to 0xFF4D8DFF.toInt(), "Cyan" to 0xFF29D8E8.toInt(),
    "Green" to 0xFF3DDC84.toInt(), "Pink" to 0xFFFF5FA8.toInt(), "Orange" to 0xFFFF9040.toInt(),
    "Red" to 0xFFFF5A5F.toInt()
)

@Composable
private fun Channel(label: String, value: Int, onChange: (Int) -> Unit, onDone: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("$label $value", color = FxMuted, fontSize = 12.sp, modifier = Modifier.width(64.dp))
        Slider(
            value = value.toFloat(),
            onValueChange = { onChange(it.toInt()) },
            onValueChangeFinished = onDone,
            valueRange = 0f..255f,
            modifier = Modifier.weight(1f).semantics { contentDescription = "$label channel" }
        )
    }
}

@Composable
fun ThemeCard() {
    val ctx = LocalContext.current
    val raw = FxTheme.raw
    val r = android.graphics.Color.red(raw)
    val g = android.graphics.Color.green(raw)
    val b = android.graphics.Color.blue(raw)
    val save = { FxTheme.save(ctx) }

    Column(
        Modifier.fillMaxWidth().background(FxSurface, RoundedCornerShape(16.dp)).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("Theme color", color = Color.White, fontSize = 18.sp)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(Modifier.size(28.dp).background(FxPurple, CircleShape))
            Text("#%06X".format(raw and 0xFFFFFF), color = FxMuted, fontSize = 12.sp)
        }
        Channel("R", r, { FxTheme.preview(android.graphics.Color.rgb(it, g, b)) }, save)
        Channel("G", g, { FxTheme.preview(android.graphics.Color.rgb(r, it, b)) }, save)
        Channel("B", b, { FxTheme.preview(android.graphics.Color.rgb(r, g, it)) }, save)
        if (FxTheme.wasAdjusted()) {
            Text("Very dark colors are brightened so text stays readable.", color = FxMuted, fontSize = 12.sp)
        }
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            presets.forEach { (name, c) ->
                Box(
                    Modifier.size(40.dp).background(Color(c), CircleShape)
                        .clickable { FxTheme.preview(c); FxTheme.save(ctx) }
                        .semantics { contentDescription = "$name theme" }
                )
            }
        }
        TextButton(onClick = { FxTheme.reset(ctx) }) { Text("Reset to default") }
    }
}
