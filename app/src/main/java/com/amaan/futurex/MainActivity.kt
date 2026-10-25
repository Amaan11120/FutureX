package com.amaan.futurex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Purple = Color(0xFFB66CFF)
private val Background = Color(0xFF0B0715)
private val SurfaceDark = Color(0xFF191126)
private val Muted = Color(0xFFB9ADC9)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Purple,
                    background = Background,
                    surface = SurfaceDark
                )
            ) {
                FutureXHome()
            }
        }
    }
}

@Composable
fun FutureXHome() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FutureX",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Made by Amaan",
            color = Purple,
            fontSize = 14.sp
        )
        Text(
            text = "Foundation build. Features come next.",
            color = Muted,
            fontSize = 12.sp
        )
    }
}
