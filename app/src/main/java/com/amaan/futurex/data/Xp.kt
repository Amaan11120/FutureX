package com.amaan.futurex.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "xp_events")
data class XpEvent(@PrimaryKey val id: String, val xp: Int, val at: Long)

@Entity(tableName = "achievements")
data class Achievement(@PrimaryKey val id: String, val unlockedAt: Long)

@Dao
interface XpDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(e: XpEvent): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlock(a: Achievement): Long

    @Query("SELECT COALESCE(SUM(xp), 0) FROM xp_events")
    fun totalXp(): Flow<Int>

    @Query("SELECT * FROM achievements")
    fun achievements(): Flow<List<Achievement>>
}
