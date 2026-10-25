package com.amaan.futurex.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {
    @Query("SELECT * FROM exams ORDER BY dateEpochDay ASC")
    fun observeAll(): Flow<List<Exam>>

    @Insert suspend fun insert(exam: Exam): Long
    @Delete suspend fun delete(exam: Exam)
}
