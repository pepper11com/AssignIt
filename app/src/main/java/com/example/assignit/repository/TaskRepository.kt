package com.example.assignit.repository

import android.util.Log
import com.example.assignit.model.Group
import com.example.assignit.model.Task
import com.example.assignit.util.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TaskRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
){

    suspend fun createTask(task: Task): Resource<Group> {
        return withContext(Dispatchers.IO) {  // Dispatchers.IO is typically used for disk and network I/O off the main thread.
            try {
                firebaseFirestore.collection(TASK_COLLECTION)
                    .document(task.id)
                    .set(task)
                    .await()

                Log.d("TaskRepository", "task.groupId: ${task.groupId}")

                val groupSnapshot = firebaseFirestore.collection(GROUP_COLLECTION).document(task.groupId)
                    .get()
                    .await()

                Log.d("TaskRepository", "groupSnapshot: $groupSnapshot")

                val group = groupSnapshot.toObject(Group::class.java)

                Log.d("TaskRepository", "group: $group")

                return@withContext if (group != null) {
                    val updatedGroup = group.addTask(task)

                    Log.d("TaskRepository", "updatedGroup: $updatedGroup")

                    groupSnapshot.reference.set(updatedGroup).await()
                    Resource.Success(updatedGroup)
                } else {
                    Resource.Error("Group not found")
                }

            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }



    /*
    fun getTaskData(taskId: String): Flow<Task?> {
        // Use firebaseFirestore to get the data for a specific task
    }

     */


    companion object{
        private const val TASK_COLLECTION = "tasks"
        const val GROUP_COLLECTION = "groups"
        const val INVITATION_COLLECTION = "invitations"
        private const val USER_COLLECTION = "users"
    }

}