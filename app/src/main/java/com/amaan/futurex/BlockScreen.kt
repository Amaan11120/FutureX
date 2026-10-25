package com.amaan.futurex

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

@Composable
fun BlockScreen() {
    val ctx = LocalContext.current
    val act = ctx as androidx.activity.ComponentActivity
    var tick by remember { mutableStateOf(0) }
    DisposableEffect(act) {
        val o = androidx.lifecycle.LifecycleEventObserver { _, ev ->
            if (ev == androidx.lifecycle.Lifecycle.Event.ON_RESUME) tick++
        }
        act.lifecycle.addObserver(o)
        onDispose { act.lifecycle.removeObserver(o) }
    }
    val usage = remember(tick) { hasUsageAccess(ctx) }
    val overlay = remember(tick) { hasOverlay(ctx) }
    var on by remember { mutableStateOf(Prefs.blockOn(ctx)) }
    var picked by remember { mutableStateOf(Prefs.blocked(ctx)) }
    val apps = remember { launchableApps(ctx) }
    Column(
        Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("App blocking", color = Color.White, fontSize = 24.sp)
        Text("A nudge, not a lock: you can switch it off.", color = FxMuted, fontSize = 12.sp)
        if (!usage) Button(onClick = {
            ctx.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }, modifier = Modifier.fillMaxWidth()) { Text("1. Allow Usage access") }
        if (!overlay) Button(onClick = {
            val u = Uri.parse("package:" + ctx.packageName)
            ctx.startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, u))
        }, modifier = Modifier.fillMaxWidth()) { Text("2. Allow Display over other apps") }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = on,
                enabled = usage && overlay,
                onCheckedChange = { v ->
                    on = v
                    Prefs.setBlockOn(ctx, v)
                    val s = Intent(ctx, BlockService::class.java)
                    if (v) ContextCompat.startForegroundService(ctx, s) else ctx.stopService(s)
                }
            )
            Text("  Blocking on", color = Color.White)
        }
        Text("Apps to block (" + picked.size + " selected)", color = FxMuted)
        LazyColumn(Modifier.weight(1f)) {
            items(apps) { a ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = a.pkg in picked, onCheckedChange = { c ->
                        picked = if (c) picked + a.pkg else picked - a.pkg
                        Prefs.setBlocked(ctx, picked)
                    })
                    Text(a.label, color = Color.White)
                }
            }
        }
    }
}
