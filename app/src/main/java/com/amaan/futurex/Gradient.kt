package com.amaan.futurex

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.fxGradient(): Modifier = background(
    Brush.verticalGradient(
        listOf(Color(0xFF2A1250), FxBackground, FxBackground)
    )
)
