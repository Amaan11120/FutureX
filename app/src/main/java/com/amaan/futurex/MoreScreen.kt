package com.amaan.futurex

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MoreScreen(taskVm: TaskViewModel) {
    var page by remember { mutableStateOf(0) }
    Column(Modifier.fillMaxSize().fxGradient()) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = page == 0, onClick = { page = 0 }, label = { Text("Focus") })
            FilterChip(selected = page == 1, onClick = { page = 1 }, label = { Text("Settings") })
            FilterChip(selected = page == 2, onClick = { page = 2 }, label = { Text("Progress") })
        }
        when (page) { 0 -> FocusScreen(); 1 -> SettingsScreen(); else -> ProgressScreen(taskVm) }
    }
}
