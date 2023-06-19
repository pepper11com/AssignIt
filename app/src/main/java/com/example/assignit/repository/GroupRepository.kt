package com.example.assignit.repository

import com.example.assignit.model.Group
import com.example.assignit.model.Task
import com.example.assignit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
){

    //Group Data:

    fun createGroup(group: Group) {
        // Use firebaseFirestore to create a new group
    }

    /*
    fun getGroupData(groupId: String): Flow<Group?> {
        // Use firebaseFirestore to get the data for a specific group
    }

     */



    companion object{

    }

}