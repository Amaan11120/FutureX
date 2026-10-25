package com.amaan.futurex

import android.content.Context
import android.content.Intent

data class AppInfo(val label: String, val pkg: String)

fun launchableApps(c: Context): List<AppInfo> {
    val pm = c.packageManager
    val i = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
    return pm.queryIntentActivities(i, 0)
        .map { AppInfo(it.loadLabel(pm).toString(), it.activityInfo.packageName) }
        .filter { it.pkg != c.packageName }
        .filter { it.pkg != "com.android.settings" }
        .distinctBy { it.pkg }
        .sortedBy { it.label.lowercase() }
}
