package com.amaan.futurex

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaan.futurex.data.AppDatabase

private val badges = listOf(
    Triple("first_task", "First Task", "Complete any task"),
    Triple("first_focus", "First Focus Session", "Finish a focus round"),
    Triple("streak7", "7-Day Streak", "Study 7 days in a row"),
    Triple("daily_goal", "Daily Goal Reached", "Hit your daily study goal")
)

@Composable
fun AchievementsScreen() {
    val ctx = LocalContext.current
    val dao = remember { AppDatabase.get(ctx).xpDao() }
    val xp by remember { dao.totalXp() }.collectAsState(0)
    val unlocked by remember { dao.achievements() }.collectAsState(emptyList())
    val ids = unlocked.map { it.id }.toSet()
    val level = XpLogic.level(xp)

    Column(
        Modifier.fillMaxSize().fxGradient().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Achievements", color = Color.White, fontSize = 28.sp)
        Text("Level $level  -  $xp XP", color = Color.White, fontSize = 20.sp)
        LinearProgressIndicator(
            progress = XpLogic.progress(xp),
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = FxPurple, trackColor = FxSurface
        )
        Text("${XpLogic.levelStart(level + 1) - xp} XP to level ${level + 1}", color = FxMuted, fontSize = 12.sp)
        Text("Task = ${XpLogic.TASK_XP} XP. Focus = 1 XP per minute. Each counts once.", color = FxMuted, fontSize = 12.sp)
        badges.forEach { (id, title, desc) ->
            val on = id in ids
            Column(
                Modifier.fillMaxWidth().background(if (on) FxPurple.copy(alpha = 0.25f) else FxSurface, RoundedCornerShape(16.dp)).padding(14.dp)
            ) {
                Text(title, color = if (on) Color.White else FxMuted, fontSize = 16.sp)
                Text(if (on) "Unlocked" else "Locked - $desc", color = FxMuted, fontSize = 12.sp)
            }
        }
    }
}
