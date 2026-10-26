package com.amaan.futurex

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

fun Modifier.neon(radius: Int = 16): Modifier {
    val shape = RoundedCornerShape(radius.dp)
    return shadow(10.dp, shape, ambientColor = FxPurple, spotColor = FxPurple)
        .border(1.dp, FxPurple, shape)
}
