package com.amaan.futurex

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlaceholderScreen(title: String) {
    Column(
        Modifier.fillMaxSize().fxGradient().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, color = Color.White, fontSize = 28.sp)
        Text("Coming in a later step.", color = FxMuted)
    }
}
