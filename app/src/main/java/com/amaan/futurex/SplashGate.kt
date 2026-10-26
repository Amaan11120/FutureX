package com.amaan.futurex

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashGate(content: @Composable () -> Unit) {
    var show by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1500)
        show = false
    }
    if (!show) {
        content()
        return
    }
    Column(
        Modifier.fillMaxSize().fxGradient(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painterResource(R.drawable.futurex_logo), "FutureX logo", Modifier.size(220.dp))
        Text("Plan - Study - Achieve", color = FxMuted, fontSize = 14.sp)
        Text("Made by Amaan", color = FxPurple, fontSize = 12.sp)
    }
}
