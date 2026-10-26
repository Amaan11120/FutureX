package com.amaan.futurex.data

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey val id: String,
    val dayEpoch: Long,
    val startedAt: Long,
    val minutes: Int,
    val subject: String? = null
)

@Dao
interface FocusSessionDao {
    // Returns -1 when the id already exists, so a session is never counted twice.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(s: FocusSession): Long

    @Query("SELECT COALESCE(SUM(minutes), 0) FROM focus_sessions WHERE dayEpoch = :day AND id NOT LIKE 'legacy-%'")
    suspend fun realMinutes(day: Long): Int

    @Query("SELECT * FROM focus_sessions ORDER BY startedAt")
    fun all(): Flow<List<FocusSession>>
}
