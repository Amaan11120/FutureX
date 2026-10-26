package com.amaan.futurex

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.amaan.futurex.data.AppDatabase
import kotlinx.coroutines.flow.first
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class TaskDueWorker(private val ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val id = inputData.getLong("id", 0)
        val t = AppDatabase.get(ctx).studyTaskDao().observeAll().first().firstOrNull { it.id == id }
        if (t == null || t.isDone) return Result.success()
        val nm = ctx.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= 26) {
            nm.createNotificationChannel(NotificationChannel("tasks", "Task reminders", NotificationManager.IMPORTANCE_DEFAULT))
        }
        val n = NotificationCompat.Builder(ctx, "tasks")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Task due now")
            .setContentText(t.title)
            .build()
        nm.notify(200000 + id.toInt(), n)
        return Result.success()
    }
}

object TaskAlarm {
    private fun name(id: Long) = "task-due-" + id
    fun cancel(c: Context, id: Long) { WorkManager.getInstance(c).cancelUniqueWork(name(id)) }
    fun schedule(c: Context, id: Long, day: Long, minutes: Int) {
        val at = LocalDate.ofEpochDay(day).atTime(minutes / 60, minutes % 60)
        val ms = Duration.between(LocalDateTime.now(), at).toMillis()
        if (ms <= 0) return
        val req = OneTimeWorkRequestBuilder<TaskDueWorker>()
            .setInitialDelay(ms, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("id" to id)).build()
        WorkManager.getInstance(c).enqueueUniqueWork(name(id), ExistingWorkPolicy.REPLACE, req)
    }
}
