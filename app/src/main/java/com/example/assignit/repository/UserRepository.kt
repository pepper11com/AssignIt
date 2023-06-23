package com.example.assignit.repository

import android.util.Log
import com.example.assignit.model.User
import com.example.assignit.util.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
){
    private val currentUser = MutableStateFlow(firebaseAuth.currentUser)

    init {
        firebaseAuth.addAuthStateListener { firebaseAuth ->
            currentUser.value = firebaseAuth.currentUser
        }
    }

    //User Authentication:

    suspend fun signIn(email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if(result.user != null) emit(Resource.Success(Unit))
            else emit(Resource.Error("Failed to sign in"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        currentUser.value = null
    }


    suspend fun createUser(email: String, username: String, password: String): Resource<Unit> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                firebaseFirestore.collection(USER_COLLECTION).document(user.uid).set(User(user.uid, username, email)).await()
             Resource.Success(Unit)
            } else {
                Resource.Error("Failed to create user")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }


    //User Data:

    suspend fun getUserData(): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val uid = firebaseAuth.currentUser?.uid
            if (uid != null) {
                val doc = firebaseFirestore.collection(USER_COLLECTION).document(uid).get().await()
                val user = doc.toObject(User::class.java)
                if (user != null) emit(Resource.Success(user))
                else emit(Resource.Error("Failed to get user data"))
            } else {
                emit(Resource.Error("User not logged in"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun updateUserData(user: User): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val uid = firebaseAuth.currentUser?.uid
            if (uid != null) {
                firebaseFirestore.collection(USER_COLLECTION).document(uid).set(user).await()
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("User not logged in"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun isUsernameAvailable(username: String): Boolean {
        val querySnapshot = firebaseFirestore.collection(USER_COLLECTION)
            .whereEqualTo("username", username)
            .limit(1)
            .get()
            .await()
        return querySnapshot.isEmpty
    }

    suspend fun isEmailAvailable(email: String): Boolean {
        val querySnapshot = firebaseFirestore.collection(USER_COLLECTION)
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .await()
        return querySnapshot.isEmpty
    }


    companion object{
        private const val USER_COLLECTION = "users"
    }

}