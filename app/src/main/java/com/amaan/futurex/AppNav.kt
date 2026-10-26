package com.amaan.futurex

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.sp

private data class Tab(val label: String, val icon: ImageVector)

private val tabs = listOf(
    Tab("Home", Icons.Filled.Home),
    Tab("Tasks", Icons.Filled.CheckCircle),
    Tab("Exams", Icons.Filled.DateRange),
    Tab("Planner", Icons.Filled.Star),
    Tab("Settings", Icons.Filled.Settings),
    Tab("More", Icons.Filled.Menu)
)

@Composable
fun AppNav(taskVm: TaskViewModel, examVm: ExamViewModel, onSignedOut: () -> Unit) {
    val ctx = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var tab by remember { mutableStateOf(0) }
    val reduced = remember {
        android.provider.Settings.Global.getFloat(
            ctx.contentResolver, android.provider.Settings.Global.ANIMATOR_DURATION_SCALE, 1f
        ) == 0f
    }
    Scaffold(bottomBar = {
        NavigationBar {
            tabs.forEachIndexed { i, t ->
                NavigationBarItem(
                    selected = tab == i,
                    onClick = {
                        if (tab != i) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        tab = i
                    },
                    icon = { Icon(t.icon, t.label) },
                    label = { Text(t.label, maxLines = 1, fontSize = 11.sp) }
                )
            }
        }
    }) { pad ->
        Box(Modifier.padding(pad)) {
            Crossfade(targetState = tab, animationSpec = tween(if (reduced) 0 else 180), label = "tabs") { t ->
                when (t) {
                    0 -> HomeScreen(taskVm, examVm)
                    1 -> TasksScreen(taskVm)
                    2 -> ExamsScreen(examVm)
                    3 -> PlannerScreen(taskVm)
                    4 -> SettingsScreen(onSignedOut)
                    5 -> MoreScreen(taskVm, onSignedOut)
                    else -> PlaceholderScreen(tabs[t].label)
                }
            }
        }
    }
}
