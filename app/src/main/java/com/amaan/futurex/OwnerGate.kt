package com.amaan.futurex

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaan.futurex.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OwnerGate(uid: String, onSignedOut: () -> Unit, content: @Composable () -> Unit) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val sp = remember { ctx.getSharedPreferences("fx_owner", Context.MODE_PRIVATE) }
    val owner = remember(uid) { sp.getString("uid", null) }
    var allowed by remember(uid) { mutableStateOf(owner == null || owner == uid) }
    var busy by remember { mutableStateOf(false) }

    LaunchedEffect(uid) {
        if (owner == null) sp.edit().putString("uid", uid).apply()
    }

    if (allowed) {
        content()
        return
    }

    Column(
        Modifier.fillMaxSize().fxGradient().verticalScroll(rememberScrollState()).padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Different account", color = Color.White, fontSize = 26.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(12.dp))
        Text(
            "This phone holds study data saved by another Google account. " +
                "To keep it private, you can sign out, or erase this phone's tasks and exams and start fresh.",
            color = FxMuted, fontSize = 15.sp, textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { scope.launch { AuthManager.signOut(ctx); onSignedOut() } },
            enabled = !busy,
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
        ) { Text("Sign out (keep data)") }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(
            onClick = {
                busy = true
                scope.launch {
                    withContext(Dispatchers.IO) { AppDatabase.get(ctx).clearAllTables() }
                    sp.edit().putString("uid", uid).apply()
                    allowed = true
                    busy = false
                }
            },
            enabled = !busy,
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
        ) { Text("Erase this phone's data and continue") }
    }
}
