package com.example.assignit.repository

import com.example.assignit.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TaskRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
){

    fun createTask(task: Task) {
        // Use firebaseFirestore to create a new task
    }

    /*
    fun getTaskData(taskId: String): Flow<Task?> {
        // Use firebaseFirestore to get the data for a specific task
    }

     */


    companion object{

    }

}