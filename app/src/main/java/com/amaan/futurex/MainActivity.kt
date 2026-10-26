package com.amaan.futurex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    private val taskVm: TaskViewModel by viewModels()
    private val examVm: ExamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ReminderScheduler.schedule(this)
        Thread { kotlinx.coroutines.runBlocking { LegacyImport.run(applicationContext); FocusSettle.run(applicationContext) } }.start()
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            requestPermissions(arrayOf("android.permission.POST_NOTIFICATIONS"), 1)
        }
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = FxPurple,
                    background = FxBackground,
                    surface = FxSurface
                )
            ) {
                SplashGate {
                    var user by remember { mutableStateOf(AuthManager.currentUser()) }
                    val u = user
                    if (u == null) LoginScreen { user = AuthManager.currentUser() } else OwnerGate(u.uid, { user = null }) { AppNav(taskVm, examVm) { user = null } }
                }
            }
        }
    }
}
