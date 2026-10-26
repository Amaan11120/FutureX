package com.amaan.futurex

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

sealed class SignInResult {
    object Success : SignInResult()
    object Cancelled : SignInResult()
    class Failed(val message: String) : SignInResult()
}

object AuthManager {
    fun currentUser() = FirebaseAuth.getInstance().currentUser

    suspend fun signIn(ctx: Context): SignInResult {
        return try {
            val option = GetSignInWithGoogleOption.Builder(
                ctx.getString(R.string.default_web_client_id)
            ).build()
            val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
            val cred = CredentialManager.create(ctx).getCredential(ctx, request).credential
            if (cred is CustomCredential &&
                cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val token = GoogleIdTokenCredential.createFrom(cred.data).idToken
                FirebaseAuth.getInstance()
                    .signInWithCredential(GoogleAuthProvider.getCredential(token, null))
                    .await()
                SignInResult.Success
            } else {
                SignInResult.Failed("Unexpected sign-in response. Please try again.")
            }
        } catch (e: GetCredentialCancellationException) {
            SignInResult.Cancelled
        } catch (e: Exception) {
            SignInResult.Failed("Sign-in failed. Check your connection and try again.")
        }
    }

    suspend fun signOut(ctx: Context) {
        FirebaseAuth.getInstance().signOut()
        try {
            CredentialManager.create(ctx).clearCredentialState(ClearCredentialStateRequest())
        } catch (_: Exception) {
        }
    }
}
