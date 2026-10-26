package com.amaan.futurex

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

object FxTheme {
    private val DEFAULT = 0xFFB66CFF.toInt()

    var raw by mutableStateOf(DEFAULT)
        private set

    private fun prefs(c: Context) = c.getSharedPreferences("fx_theme", Context.MODE_PRIVATE)
    fun init(c: Context) { raw = prefs(c).getInt("raw", DEFAULT) }
    fun preview(rgb: Int) { raw = rgb or 0xFF000000.toInt() }
    fun save(c: Context) { prefs(c).edit().putInt("raw", raw).apply() }
    fun reset(c: Context) { raw = DEFAULT; save(c) }

    // Very dark picks are brightened so buttons and text stay readable.
    private fun fix(argb: Int): Int {
        if (argb == DEFAULT) return argb
        val h = FloatArray(3)
        android.graphics.Color.colorToHSV(argb, h)
        if (h[2] < 0.6f) h[2] = 0.6f
        return android.graphics.Color.HSVToColor(h)
    }
    fun wasAdjusted() = fix(raw) != raw

    private fun tone(sMul: Float, sMax: Float, v: Float): Color {
        val h = FloatArray(3)
        android.graphics.Color.colorToHSV(fix(raw), h)
        h[1] = (h[1] * sMul).coerceAtMost(sMax)
        h[2] = v
        return Color(android.graphics.Color.HSVToColor(h))
    }

    val primary: Color get() = Color(fix(raw))
    val background: Color get() = if (raw == DEFAULT) Color(0xFF0B0715) else tone(1.1f, 0.7f, 0.08f)
    val surface: Color get() = if (raw == DEFAULT) Color(0xFF191126) else tone(1.0f, 0.65f, 0.15f)
    val top: Color get() = if (raw == DEFAULT) Color(0xFF2A1250) else tone(1.35f, 0.85f, 0.31f)
    val muted: Color get() = if (raw == DEFAULT) Color(0xFFB9ADC9) else tone(0.25f, 0.2f, 0.82f)
}

val FxPurple: Color get() = FxTheme.primary
val FxBackground: Color get() = FxTheme.background
val FxSurface: Color get() = FxTheme.surface
val FxMuted: Color get() = FxTheme.muted
val FxTop: Color get() = FxTheme.top
