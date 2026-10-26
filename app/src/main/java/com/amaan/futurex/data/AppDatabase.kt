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
    entities = [StudyTask::class, Exam::class, FocusSession::class, XpEvent::class, Achievement::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studyTaskDao(): StudyTaskDao
    abstract fun examDao(): ExamDao
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun xpDao(): XpDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "futurex.db"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                    .build().also { instance = it }
            }
    }
}

private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `study_tasks` ADD COLUMN `completedAt` INTEGER")
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `focus_sessions` (" +
                "`id` TEXT NOT NULL, " +
                "`dayEpoch` INTEGER NOT NULL, " +
                "`startedAt` INTEGER NOT NULL, " +
                "`minutes` INTEGER NOT NULL, " +
                "`subject` TEXT, " +
                "PRIMARY KEY(`id`))"
        )
    }
}

private val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `xp_events` (`id` TEXT NOT NULL, `xp` INTEGER NOT NULL, `at` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        db.execSQL("CREATE TABLE IF NOT EXISTS `achievements` (`id` TEXT NOT NULL, `unlockedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))")
    }
}
