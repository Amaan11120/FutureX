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

class StudyWorker(private val ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val nm = ctx.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= 26) {
            val ch = NotificationChannel("study", "Study reminder", NotificationManager.IMPORTANCE_DEFAULT)
            nm.createNotificationChannel(ch)
        }
        val n = NotificationCompat.Builder(ctx, "study")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Time to study")
            .setContentText("Open FutureX and start a focus session.")
            .build()
        nm.notify(9001, n)
        return Result.success()
    }
}

object StudyReminder {
    private const val NAME = "study-reminder"
    fun enable(c: Context, hour: Int, minute: Int) {
        val now = LocalDateTime.now()
        var next = now.toLocalDate().atTime(hour, minute)
        if (!next.isAfter(now)) next = next.plusDays(1)
        val mins = Duration.between(now, next).toMinutes()
        val req = PeriodicWorkRequestBuilder<StudyWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(mins, TimeUnit.MINUTES).build()
        WorkManager.getInstance(c).enqueueUniquePeriodicWork(
            NAME, ExistingPeriodicWorkPolicy.UPDATE, req
        )
    }
    fun disable(c: Context) { WorkManager.getInstance(c).cancelUniqueWork(NAME) }
}
