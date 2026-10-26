package com.amaan.futurex

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun BackupCard() {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    var msg by remember { mutableStateOf("") }
    val save = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) scope.launch {
            msg = try {
                val text = Backup.export(ctx)
                withContext(Dispatchers.IO) {
                    ctx.contentResolver.openOutputStream(uri)?.use { it.write(text.toByteArray()) }
                }
                "Backup saved."
            } catch (e: Exception) { "Backup failed: " + e.message }
        }
    }
    val open = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) scope.launch {
            msg = try {
                val text = withContext(Dispatchers.IO) {
                    ctx.contentResolver.openInputStream(uri)?.use { String(it.readBytes()) } ?: ""
                }
                "Restored " + Backup.restore(ctx, text) + " new item(s)."
            } catch (e: Exception) { "Restore failed: " + e.message }
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Backup and restore", color = FxMuted, fontSize = 12.sp)
        Button(onClick = { save.launch("futurex-backup.json") }, modifier = Modifier.fillMaxWidth()) {
            Text("Save backup file")
        }
        OutlinedButton(onClick = { open.launch(arrayOf("*/*")) }, modifier = Modifier.fillMaxWidth()) {
            Text("Restore from backup file")
        }
        if (msg.isNotEmpty()) Text(msg, color = FxMuted)
    }
}
