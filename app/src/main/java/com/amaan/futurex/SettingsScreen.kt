package com.amaan.futurex

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(onSignedOut: () -> Unit) {
    val ctx = LocalContext.current
    var name by remember { mutableStateOf(Prefs.name(ctx)) }
    var goal by remember { mutableStateOf(Prefs.goalHours(ctx).toString()) }
    var saved by remember { mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize().fxGradient().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Settings", color = Color.White, fontSize = 28.sp)
        Text("Saved on this phone only", color = FxMuted, fontSize = 12.sp)
        OutlinedTextField(
            value = name, onValueChange = { name = it; saved = false },
            label = { Text("Your name") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = goal, onValueChange = { goal = it; saved = false },
            label = { Text("Daily study goal (hours)") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        val goalOk = goal.toIntOrNull()?.let { it in 1..24 } == true
        if (!goalOk) Text("Goal must be a whole number, 1 to 24", color = FxPurple, fontSize = 12.sp)
        Button(
            onClick = {
                Prefs.setName(ctx, name)
                Prefs.setGoalHours(ctx, goal.toInt())
                saved = true
            },
            enabled = goalOk,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Save") }
        ThemeCard()
        BackupCard()
        ReminderCard()
        if (saved) Text("Saved.", color = FxMuted)
        val scope = rememberCoroutineScope()
        OutlinedButton(onClick = { scope.launch { AuthManager.signOut(ctx); onSignedOut() } }, modifier = Modifier.fillMaxWidth()) { Text("Sign out") }
        Text("FutureX 1.0 - Made by Amaan", color = FxMuted, fontSize = 12.sp)
    }
}
