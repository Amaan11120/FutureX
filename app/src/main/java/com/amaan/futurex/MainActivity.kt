
package com.amaan.futurex

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar

private val Purple = Color(0xFFB66CFF)
private val Background = Color(0xFF0B0715)
private val SurfaceDark = Color(0xFF191126)
private val Muted = Color(0xFFB9ADC9)

data class Subject(
    val name: String,
    val chapters: Int,
    val done: Int
)

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
                FutureXScreen()
            }
        }
    }
}

@Composable
fun FutureXScreen() {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("futurex", Context.MODE_PRIVATE)
    }

    var subjects by remember { mutableStateOf(readSubjects(prefs)) }
    var exam by remember { mutableStateOf(prefs.getString("exam", "") ?: "") }
    var examDate by remember {
        mutableStateOf(prefs.getString("date", "") ?: "")
    }
    var hours by remember { mutableStateOf(prefs.getString("hours", "3") ?: "3") }
    var subjectName by remember { mutableStateOf("") }
    var chapterCount by remember { mutableStateOf("5") }
    var selectedTab by remember { mutableStateOf("Home") }
    var showDialog by remember { mutableStateOf(false) }

    fun saveSubjects(list: List<Subject>) {
        subjects = list
        val array = JSONArray()
        list.forEach {
            array.put(JSONObject().apply {
                put("name", it.name)
                put("chapters", it.chapters)
                put("done", it.done)
            })
        }
        prefs.edit().putString("subjects", array.toString()).apply()
    }

    fun saveSettings() {
        prefs.edit()
            .putString("exam", exam)
            .putString("date", examDate)
            .putString("hours", hours)
            .apply()
    }

    val total = subjects.sumOf { it.chapters }
    val done = subjects.sumOf { it.done }
    val progress = if (total == 0) 0f else done.toFloat() / total

    val daysLeft = try {
        ChronoUnit.DAYS.between(
            LocalDate.now(),
            LocalDate.parse(examDate)
        ).toInt()
    } catch (_: Exception) {
        -1
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF28113C), Background, Background)
                )
            )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
        ) {
            Spacer(Modifier.height(22.dp))

            Text(
                "FutureX",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                "MADE BY AMAAN  •  YOUR STUDY SPACE",
                color = Purple,
                fontSize = 10.sp
            )

            Spacer(Modifier.height(18.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("Home", "Syllabus", "Plan", "Settings").forEach { tab ->
                    FilterChip(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        label = { Text(tab, fontSize = 10.sp) }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            when (selectedTab) {
                "Home" -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = SurfaceDark
                                ),
                                shape = RoundedCornerShape(22.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(Modifier.padding(20.dp)) {
                                    Text(
                                        "YOUR NEXT EXAM",
                                        color = Purple,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        exam.ifBlank { "Set your exam in Settings" },
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        when {
                                            daysLeft > 0 -> "$daysLeft days left"
                                            daysLeft == 0 -> "Exam is today!"
                                            daysLeft < -1 -> "Exam date has passed"
                                            else -> "Your exam countdown starts here"
                                        },
                                        color = Muted
                                    )
                                }
                            }
                        }

                        item {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Stat("DAILY TARGET", "$hours hours", Modifier.weight(1f))
                                Stat("CHAPTERS DONE", "$done / $total", Modifier.weight(1f))
                            }
                        }

                        item {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = SurfaceDark
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Column(Modifier.padding(18.dp)) {
                                    Text(
                                        "Syllabus progress",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(12.dp))
                                    LinearProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp),
                                        color = Purple
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        "${(progress * 100).toInt()}% completed",
                                        color = Muted
                                    )
                                }
                            }
                        }

                        item {
                            Button(
                                onClick = { selectedTab = "Plan" },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("View study plan")
                            }
                        }

                        item {
                            Text(
                                "YOUR SUBJECTS",
                                color = Muted,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (subjects.isEmpty()) {
                            item {
                                Text(
                                    "Open Syllabus to add your subjects.",
                                    color = Muted
                                )
                            }
                        } else {
                            items(subjects) { subject ->
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = SurfaceDark
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Column(Modifier.padding(16.dp)) {
                                        Text(
                                            subject.name,
                                            color = Color.White,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            "${subject.done}/${subject.chapters} chapters complete",
                                            color = Muted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                "Syllabus" -> {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Your syllabus",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Add subjects and track your chapters.",
                            color = Muted
                        )
                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = { showDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("+ Add subject")
                        }

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(subjects) { subject ->
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = SurfaceDark
                                    ),
                                    shape = RoundedCornerShape(18.dp)
                                ) {
                                    Column(Modifier.padding(14.dp)) {
                                        Text(
                                            subject.name,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                        Text(
                                            "${subject.done}/${subject.chapters} completed",
                                            color = Muted
                                        )
                                        Row(
                                            horizontalArrangement =
                                                Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = {
                                                    val updated = subjects.map {
                                                        if (it.name == subject.name &&
                                                            it.done < it.chapters
                                                        ) it.copy(done = it.done + 1)
                                                        else it
                                                    }
                                                    saveSubjects(updated)
                                                },
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Complete chapter")
                                            }
                                            OutlinedButton(
                                                onClick = {
                                                    saveSubjects(
                                                        subjects.filterNot {
                                                            it.name == subject.name
                                                        }
                                                    )
                                                }
                                            ) {
                                                Text("Delete")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "Plan" -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                "Today's study plan",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Suggested sessions based on your available hours.",
                                color = Muted
                            )
                        }

                        val unfinished = subjects.filter { it.done < it.chapters }

                        if (unfinished.isEmpty()) {
                            item {
                                Text(
                                    "Add subjects with unfinished chapters first.",
                                    color = Muted
                                )
                            }
                        } else {
                            items(unfinished) { subject ->
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = SurfaceDark
                                    ),
                                    shape = RoundedCornerShape(18.dp)
                                ) {
                                    Column(Modifier.padding(16.dp)) {
                                        Text(
                                            subject.name,
                                            color = Purple,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            "${subject.chapters - subject.done} chapters remaining",
                                            color = Muted
                                        )
                                        Text(
                                            "Suggested focus: 25 minutes, then take a break.",
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                "Starter plan only. Smart scheduling and reminders will be added in later updates.",
                                color = Muted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                "Settings" -> {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Study settings",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(16.dp))

                        OutlinedTextField(
                            value = exam,
                            onValueChange = { exam = it },
                            label = { Text("Exam name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                val c = Calendar.getInstance()
                                DatePickerDialog(
                                    context,
                                    { _, year, month, day ->
                                        examDate = String.format(
                                            "%04d-%02d-%02d",
                                            year,
                                            month + 1,
                                            day
                                        )
                                    },
                                    c.get(Calendar.YEAR),
                                    c.get(Calendar.MONTH),
                                    c.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                if (examDate.isBlank()) "Choose exam date"
                                else "Exam date: $examDate"
                            )
                        }

     
