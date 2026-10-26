package com.amaan.futurex

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onSignedIn: () -> Unit) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier.fillMaxSize().fxGradient().verticalScroll(rememberScrollState()).padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painterResource(R.drawable.futurex_logo), "FutureX logo", Modifier.size(160.dp))
        Spacer(Modifier.height(16.dp))
        Text("Welcome to FutureX", color = Color.White, fontSize = 28.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text("Your study journey starts here.", color = FxMuted, fontSize = 16.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                if (loading) return@Button
                loading = true
                error = null
                scope.launch {
                    when (val r = AuthManager.signIn(ctx)) {
                        is SignInResult.Success -> onSignedIn()
                        is SignInResult.Cancelled -> error = "Sign-in was cancelled. Tap to try again."
                        is SignInResult.Failed -> error = r.message
                    }
                    loading = false
                }
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
        ) {
            if (loading) {
                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = Color.White)
                Spacer(Modifier.width(10.dp))
                Text("Signing in...")
            } else {
                Text("Continue with Google")
            }
        }
        error?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = Color(0xFFFF8A8A), fontSize = 14.sp, textAlign = TextAlign.Center)
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "Google is used only to sign you in. Your tasks and exams stay on this phone.",
            color = FxMuted, fontSize = 12.sp, textAlign = TextAlign.Center
        )
    }
}
