package com.amaan.futurex

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class CustomWorker(private val ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val nm = ctx.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= 26) {
            nm.createNotificationChannel(NotificationChannel("custom", "My reminders", NotificationManager.IMPORTANCE_DEFAULT))
        }
        val n = NotificationCompat.Builder(ctx, "custom")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Reminder")
            .setContentText(inputData.getString("title") ?: "")
            .build()
        nm.notify(300000 + inputData.getLong("id", 0).toInt(), n)
        return Result.success()
    }
}

object CustomReminder {
    private fun name(id: Long) = "custom-rem-" + id
    fun disable(c: Context, id: Long) { WorkManager.getInstance(c).cancelUniqueWork(name(id)) }
    fun enable(c: Context, r: Reminder) {
        val now = LocalDateTime.now()
        var next = now.toLocalDate().atTime(r.minutes / 60, r.minutes % 60)
        if (!next.isAfter(now)) next = next.plusDays(1)
        val req = PeriodicWorkRequestBuilder<CustomWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(Duration.between(now, next).toMinutes(), TimeUnit.MINUTES)
            .setInputData(workDataOf("id" to r.id, "title" to r.title)).build()
        WorkManager.getInstance(c).enqueueUniquePeriodicWork(
            name(r.id), ExistingPeriodicWorkPolicy.UPDATE, req
        )
    }
}
