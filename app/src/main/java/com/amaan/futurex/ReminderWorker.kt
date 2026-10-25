package com.amaan.futurex

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.amaan.futurex.data.AppDatabase
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class ReminderWorker(
    private val ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        val dao = AppDatabase.get(ctx).examDao()
        val today = LocalDate.now().toEpochDay()
        for (exam in dao.observeAll().first()) {
            if (exam.dateEpochDay == today) {
                notify(exam.id * 2, "Exam today", exam.name)
            } else if (exam.dateEpochDay == today + 1) {
                notify(exam.id * 2 + 1, "Exam tomorrow", exam.name)
            }
        }
        return Result.success()
    }

    private fun notify(id: Long, title: String, text: String) {
        val nm = ctx.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= 26) {
            nm.createNotificationChannel(
                NotificationChannel("exams", "Exam reminders",
                    NotificationManager.IMPORTANCE_DEFAULT)
            )
        }
        val n = NotificationCompat.Builder(ctx, "exams")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .build()
        nm.notify(id.toInt(), n)
    }
}
