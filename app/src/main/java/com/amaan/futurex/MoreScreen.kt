package com.amaan.futurex

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MoreScreen(taskVm: TaskViewModel, onSignedOut: () -> Unit) {
    var page by remember { mutableStateOf(0) }
    Column(Modifier.fillMaxSize().fxGradient()) {
        Row(Modifier.horizontalScroll(rememberScrollState()).padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = page == 0, onClick = { page = 0 }, label = { Text("Focus") })
            FilterChip(selected = page == 2, onClick = { page = 2 }, label = { Text("Progress") })
            FilterChip(selected = page == 3, onClick = { page = 3 }, label = { Text("Blocking") })
            FilterChip(selected = page == 4, onClick = { page = 4 }, label = { Text("Reminders") })
            FilterChip(selected = page == 5, onClick = { page = 5 }, label = { Text("Analytics") })
        }
        when (page) { 0 -> FocusScreen(); 1 -> SettingsScreen(onSignedOut); 2 -> ProgressScreen(taskVm); 3 -> BlockScreen(); 5 -> AnalyticsScreen(taskVm); else -> RemindersScreen() }
    }
}
