package com.ari.curve.data

import android.content.ContentValues.TAG
import android.util.Log
import com.ari.curve.data.dao.Task
import com.ari.curve.data.dao.User
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.type.DateTime
import kotlinx.coroutines.tasks.await
import kotlin.collections.mapOf

class FirebaseManager {
    private val db = Firebase.firestore
    private var allUsernames = emptyList<String>()
    suspend fun checkRegisteredUsers(email: String): Boolean{
        return try {
            val document = db.collection("users")
                .document(email)
                .get()
                .await()
            document.exists()
        } catch (e : Exception){
            Log.e(TAG, "Error checking document", e)
            false
        }
    }

    suspend fun addTask(task: Task,email: String){
        try {
            db.collection("users")
                .document(email)
                .collection("tasks")
                .document(task.taskId)
                .set(task)
                .await()
        }
        catch(e : Exception) {
            Log.e(TAG, "Error writing task", e)
        }
    }

    suspend fun removeTask(taskid: String, email: String) {
        try {
            db.collection("users")
                .document(email)
                .collection("tasks")
                .document(taskid)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting task", e)
        }
    }



    suspend fun registerNewUser(newUser : User, email : String): Boolean{
        return try{
            db.collection("users")
                .document(email)
                .set(newUser)
                .await()
            db.collection("users")
                .document("all")
                .set(mapOf(newUser.username to email), SetOptions.merge())
                .await()
            true
        }
        catch (e : Exception){
            Log.e(TAG, "Error writing document", e)
            false
        }
    }

    suspend fun getRegisteredUser(email: String): User? {
        return try {
            val documentSnapshot = db.collection("users")
                .document(email)
                .get()
                .await()

            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(User::class.java)
            } else {
                Log.w(TAG, "No user found with email: $email")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching user", e)
            null
        }
    }


    suspend fun checkValidUsername(username: String): Boolean {
        if (allUsernames.isEmpty()) {
            setUsernames()
        }
        return username in allUsernames
    }


    fun clearAllUsers(){
        allUsernames = emptyList()
    }
    suspend fun setUsernames(){
        val document = db.collection("users").document("all").get().await()
        allUsernames = document.data?.keys?.toList() ?: emptyList()
    }
    fun listenToMessages(
        userEmail: String,
        onNewTask: (List<Task>) -> Unit
    ){
        db.collection("users")
            .document(userEmail)
            .collection("tasks")
//            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener {
                    snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val tasks = snapshot.documents.mapNotNull { it.toObject(Task::class.java) }
                onNewTask(tasks)
            }
    }
}
