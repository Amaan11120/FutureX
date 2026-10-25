package com.amaan.futurex

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class BlockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(primary = FxPurple)) {
                Column(
                    Modifier.fillMaxSize().fxGradient().padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Stay focused", color = Color.White, fontSize = 32.sp)
                    Text("This app is blocked by FutureX.", color = FxMuted)
                    Button(
                        onClick = { goHome() },
                        modifier = Modifier.padding(top = 16.dp)
                    ) { Text("Go to home screen") }
                }
            }
        }
    }

    private fun goHome() {
        startActivity(
            Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        finish()
    }
}
