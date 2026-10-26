package com.amaan.futurex.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(MIGRATION_SQL)
    }
}

private const val MIGRATION_SQL =
    "CREATE TABLE IF NOT EXISTS `exams` (" +
    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
    "`name` TEXT NOT NULL, " +
    "`dateEpochDay` INTEGER NOT NULL)"

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `study_tasks` ADD COLUMN `dueMinutes` INTEGER")
    }
}
@Database(
    entities = [StudyTask::class, Exam::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studyTaskDao(): StudyTaskDao
    abstract fun examDao(): ExamDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "futurex.db"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build().also { instance = it }
            }
    }
}
