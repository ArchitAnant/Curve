package com.ari.curve.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel(
//    private val firebaseManager: FirebaseManager,
    private val navHostController: NavHostController
): ViewModel() {
    var currentUserEmail: String? = null
    var firebaseIdToken: String? = null

    fun onGetCredentialResponse(context: Context, credential: Credential) {
        viewModelScope.launch {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val firebaseCred =
                    GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                val authResult = Firebase.auth.signInWithCredential(firebaseCred).await()

                currentUserEmail = authResult.user?.email
                Log.d("credential", currentUserEmail.toString())

                if (currentUserEmail != null) {

                }
            }
        }
    }
}