package com.amaan.futurex

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

private data class Tab(val label: String, val icon: ImageVector)

private val tabs = listOf(
    Tab("Home", Icons.Filled.Home),
    Tab("Tasks", Icons.Filled.CheckCircle),
    Tab("Exams", Icons.Filled.DateRange),
    Tab("Planner", Icons.Filled.Star),
    Tab("More", Icons.Filled.Menu)
)

@Composable
fun AppNav(taskVm: TaskViewModel, examVm: ExamViewModel) {
    var tab by remember { mutableStateOf(0) }
    Scaffold(bottomBar = {
        NavigationBar {
            tabs.forEachIndexed { i, t ->
                NavigationBarItem(
                    selected = tab == i,
                    onClick = { tab = i },
                    icon = { Icon(t.icon, t.label) },
                    label = { Text(t.label) }
                )
            }
        }
    }) { pad ->
        Box(Modifier.padding(pad)) {
            when (tab) {
                0 -> HomeScreen(taskVm, examVm)
                1 -> TasksScreen(taskVm)
                2 -> ExamsScreen(examVm)
                3 -> PlannerScreen(taskVm)
                4 -> MoreScreen(taskVm)
                else -> PlaceholderScreen(tabs[tab].label)
            }
        }
    }
}
