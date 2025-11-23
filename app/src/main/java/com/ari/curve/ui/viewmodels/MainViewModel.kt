package com.ari.curve.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ari.curve.data.FirebaseManager
import com.ari.curve.data.GeminiRepo
import com.ari.curve.data.UserCache
import com.ari.curve.data.dao.Task
import com.ari.curve.data.dao.User
import com.ari.curve.ui.naviagtions.Screen
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

enum class regState {
    waiting,
    success,
    failed
}

class MainViewModel(
    private val firebaseManager: FirebaseManager,
    private val navHostController: NavHostController,
    private val geminiRepo: GeminiRepo,
): ViewModel() {
    var currentUserEmail: String? = null
    var currUser : User? = null

    private val _tasks = MutableStateFlow<List<Task>>(mutableListOf())
    val tasks = _tasks.asStateFlow()

    private val _successRegistered = MutableStateFlow(mutableStateOf(regState.waiting))
    var successRegistered = _successRegistered.asStateFlow()

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
//                    navHostController.navigate(Screen.wait_screen.route)
                    if (isUserRegistered(currentUserEmail.toString())){
                        currUser = firebaseManager.getRegisteredUser(currentUserEmail!!)
                        _successRegistered.value.value = regState.success
                        //log the user for debug
                        pushUserToCache(context,currUser!!,currentUserEmail.toString())
                        Log.d("reg_user",currUser.toString())
                        navigateHomeScreen()
                    }
                    else{
                        _successRegistered.value.value = regState.waiting
                        Log.d("credential","not registered")
                        val newUser = User(
                            email = authResult.user?.email.toString(),
                            username = authResult.user?.email.toString(),
                            img_url = authResult.user?.photoUrl.toString()
                        )
                        firebaseManager.registerNewUser(newUser,currentUserEmail!!)
                        navigateHomeScreen()
                    }
                }
            }
        }
    }

    suspend fun pushUserToCache(context: Context, user: User, email: String){
        UserCache.saveUser(context,user,email)
    }
    suspend fun isUserRegistered(email: String) : Boolean{
        return firebaseManager.checkRegisteredUsers(email)
    }
    fun checkUsername(username: String): Boolean{
        var userState = false
        viewModelScope.launch {
            val exists = firebaseManager.checkValidUsername(username)
            if (exists) {

                Log.d("Firestore", "Username already exists")
            } else {
                userState = true
                Log.d("Firestore", "Username available")
            }
        }
        return userState
    }

    fun clearUsernames(){
        firebaseManager.clearAllUsers()
    }
    fun navigateHomeScreen(){
        navHostController.navigate(Screen.mainChatScreen.route)

    }

    fun getRegisteredUser(email: String){
        viewModelScope.launch {
            currUser = firebaseManager.getRegisteredUser(email)
            Log.d("user_fetch",currUser.toString())
        }
    }

    fun generateTasks(prompt:String): Boolean{
        var uploaded = false
        viewModelScope.launch {
            val tasks = geminiRepo.generateTasks(prompt)
            tasks.forEach { task ->
                println(task)
                firebaseManager.addTask(task,currentUserEmail.toString())
            }
            uploaded=true
        }
        return uploaded
    }

    fun observeChat(){
        firebaseManager.listenToMessages(
                currentUserEmail!!,
            ){
                Log.d("FirebaseListener","Got a new chat! ${it.size.toString()}")
                _tasks.value = it
        }
    }

    fun removeTask(taskid: String) {
        viewModelScope.launch {
            firebaseManager.removeTask(taskid,currentUserEmail!!)
        }
    }

}