package com.example.assignit.repository

import android.util.Log
import com.example.assignit.model.User
import com.example.assignit.model.UserData
import com.example.assignit.services.GoogleAuth
import com.example.assignit.util.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    private val googleAuth: GoogleAuth
) {
    val currentUser = MutableStateFlow(firebaseAuth.currentUser)

    val hasUser: Boolean
        get() = firebaseAuth.currentUser != null

    init {
        firebaseAuth.addAuthStateListener { firebaseAuth ->
            currentUser.value = firebaseAuth.currentUser
        }
    }

    //User Authentication:

    suspend fun getUserDataById(userId: String): Resource<User> {
        return try {
            val doc = firebaseFirestore.collection(USER_COLLECTION).document(userId).get().await()
            Log.d("UserRepository", "getUserDataById: $doc")
            val user = doc.toObject(User::class.java)
            if (user != null) Resource.Success(user)
            else Resource.Error("No user found, create an account or try again later")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }

    suspend fun getCurrentUser(): User {
        val firebaseUser = firebaseAuth.currentUser
            ?: throw IllegalStateException("No current user")

        return firebaseFirestore.collection(USER_COLLECTION)
            .document(firebaseUser.uid)
            .get()
            .await()
            .toObject(User::class.java)!!
    }

    suspend fun searchUsersByUsername(username: String): Resource<List<User>> {
        return try {
            val usersQuerySnapshot = firebaseFirestore.collection(USER_COLLECTION)
                .whereEqualTo("username", username)
                .get()
                .await()

            val users = usersQuerySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
            Resource.Success(users)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
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
                firebaseFirestore.collection(USER_COLLECTION).document(user.uid)
                    .set(User(user.uid, username, email)).await()
                Resource.Success(Unit)
            } else {
                Resource.Error("Failed to create user")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }

    suspend fun createGoogleUser(user: User): Resource<Unit> {
        return try {
            firebaseFirestore.collection(USER_COLLECTION).document(user.id!!)
                .set(user).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }

    suspend fun getGoogleUser(): Resource<UserData> {
        return try {
            val signedInUser = googleAuth.getSignedInUser()
            if (signedInUser == null) {
                Resource.Error("No signed in user")
            } else {
                Resource.Success(
                    UserData(
                        signedInUser.userId,
                        signedInUser.email ?: "",
                        signedInUser.username ?: "",
                        signedInUser.profilePictureUrl ?: "",
                    )
                )
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }

    suspend fun signInWithUsernameAndPassword(username: String, password: String): Resource<Unit> {
        return try {
            val userQuerySnapshot = firebaseFirestore.collection(USER_COLLECTION)
                .whereEqualTo("username", username)
                .get()
                .await()

            if(userQuerySnapshot.isEmpty) {
                return Resource.Error("No user found with this username")
            }

            val userDocument = userQuerySnapshot.documents.first()
            val email = userDocument.getString("email") ?: throw IllegalStateException("No email field in user document")

            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            if(result.user != null) Resource.Success(Unit)
            else Resource.Error("Failed to sign in")

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
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


    companion object {
        private const val USER_COLLECTION = "users"
    }

}