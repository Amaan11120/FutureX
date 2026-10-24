package com.amaan.futurex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { BuildCheckScreen() }
    }
}

@Composable
fun BuildCheckScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF1A0B2E), Color(0xFF0B0615)))
            )
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FutureX",
            color = Color(0xFFE0AAFF),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Made by Amaan",
            color = Color(0xFF9D4EDD),
            fontSize = 16.sp
        )
        Text(
            text = "Build pipeline check passed. The full app arrives in Parts 2 to 4.",
            color = Color(0xFFB8A9C9),
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}
