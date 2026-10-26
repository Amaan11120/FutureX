package com.amaan.futurex.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_tasks")
data class StudyTask(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String,
    val dueEpochDay: Long? = null,
    val isDone: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val dueMinutes: Int? = null
)
