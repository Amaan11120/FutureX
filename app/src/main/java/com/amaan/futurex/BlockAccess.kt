package com.amaan.futurex

import android.app.AppOpsManager
import android.content.Context
import android.os.Process
import android.provider.Settings

fun hasUsageAccess(c: Context): Boolean {
    val ops = c.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = ops.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), c.packageName
    )
    return mode == AppOpsManager.MODE_ALLOWED
}

fun hasOverlay(c: Context): Boolean = Settings.canDrawOverlays(c)
