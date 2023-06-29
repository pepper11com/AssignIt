package com.example.assignit.repository

import android.util.Log
import com.example.assignit.model.Group
import com.example.assignit.model.GroupDto
import com.example.assignit.model.Task
import com.example.assignit.model.TaskDto
import com.example.assignit.util.resource.Resource
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

                val groupDto = groupSnapshot.toObject(GroupDto::class.java)
                val group = groupDto?.toGroup()



                Log.d("TaskRepository", "group: $group")

                return@withContext if (group != null) {
                    val updatedGroup = group.addTask(task.id) // Update only task ID, not the whole task object

                    Log.d("TaskRepository", "updatedGroup: $updatedGroup")

                    val updatedGroupDto = GroupDto(
                        id = updatedGroup.id,
                        name = updatedGroup.name,
                        adminId = updatedGroup.adminId,
                        memberIds = updatedGroup.memberIds.toMutableList(),
                        dayAndTimeEdited = Timestamp.now(),
                        taskIds = updatedGroup.taskIds.toMutableList()
                    )
                    groupSnapshot.reference.set(updatedGroupDto).await()
                    Resource.Success(updatedGroup)
                } else {
                    Resource.Error("Group not found")
                }

            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    suspend fun getTasksByIds(taskIds: List<String>): List<Task> {
        return withContext(Dispatchers.IO) {
            try {
                val tasks = taskIds.map { taskId ->
                    async {
                        val taskSnapshot = firebaseFirestore.collection(TASK_COLLECTION)
                            .document(taskId)
                            .get()
                            .await()
                        Log.d("TaskRepository", "taskSnapshot: $taskSnapshot")
                        taskSnapshot.toObject(TaskDto::class.java)?.toTask()
                    }
                }.awaitAll()

                Log.d("TaskRepository", "tasks: $tasks")
                tasks.filterNotNull()
            } catch (e: Exception) {
                emptyList<Task>()
            }
        }
    }


    suspend fun getTasksByUser(userId: String): List<Task> {
        return withContext(Dispatchers.IO) {
            try {
                val taskSnapshot = firebaseFirestore.collection(TASK_COLLECTION)
                    .whereArrayContains("assigneeIds", userId)
                    .get()
                    .await()

                val tasks = taskSnapshot.documents.mapNotNull { it.toObject(TaskDto::class.java)?.toTask() }
                tasks
            } catch (e: Exception) {
                emptyList<Task>()
            }
        }
    }



    companion object{
        private const val TASK_COLLECTION = "tasks"
        const val GROUP_COLLECTION = "groups"
        const val INVITATION_COLLECTION = "invitations"
        private const val USER_COLLECTION = "users"
    }

}