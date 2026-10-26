package com.amaan.futurex

import android.provider.Settings
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun Modifier.movingGlow(color: Color = FxPurple, radiusScale: Float = 1.3f): Modifier {
    val ctx = LocalContext.current
    val reduced = remember {
        Settings.Global.getFloat(ctx.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
    }
    val t = rememberInfiniteTransition(label = "glow")
    val phase by t.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Reverse),
        label = "phase"
    )
    return this.drawBehind {
        val p = if (reduced) 0.5f else phase
        val r = maxOf(size.width, size.height) * radiusScale
        val c = Offset(size.width * (0.3f + 0.4f * p), size.height / 2f)
        drawCircle(
            brush = Brush.radialGradient(
                listOf(color.copy(alpha = 0.10f + 0.14f * p), Color.Transparent), c, r
            ),
            radius = r, center = c
        )
    }
}
