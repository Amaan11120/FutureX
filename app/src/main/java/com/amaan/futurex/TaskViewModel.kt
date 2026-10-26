package com.amaan.futurex

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amaan.futurex.data.AppDatabase
import com.amaan.futurex.data.StudyTask
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).studyTaskDao()

    val tasks: StateFlow<List<StudyTask>> = dao.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun add(title: String, subject: String, due: Long? = null, dueMin: Int? = null) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val newId = dao.insert(StudyTask(title = title.trim(), subject = subject.trim(), dueEpochDay = due, dueMinutes = dueMin))
            if (due != null && dueMin != null) TaskAlarm.schedule(getApplication<Application>(), newId, due, dueMin)
        }
    }

    fun toggleDone(task: StudyTask) {
        viewModelScope.launch { dao.update(task.copy(isDone = !task.isDone, completedAt = if (!task.isDone) System.currentTimeMillis() else null)) }
    }

    fun delete(task: StudyTask) {
        viewModelScope.launch { dao.delete(task); TaskAlarm.cancel(getApplication<Application>(), task.id) }
    }
}
