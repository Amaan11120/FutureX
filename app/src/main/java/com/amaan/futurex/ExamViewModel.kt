package com.amaan.futurex

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amaan.futurex.data.AppDatabase
import com.amaan.futurex.data.Exam
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExamViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).examDao()

    val exams: StateFlow<List<Exam>> =
        dao.observeAll().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun add(name: String, epochDay: Long) {
        if (name.isBlank()) return
        viewModelScope.launch {
            dao.insert(Exam(name = name.trim(), dateEpochDay = epochDay))
        }
    }

    fun delete(exam: Exam) {
        viewModelScope.launch { dao.delete(exam) }
    }
}
