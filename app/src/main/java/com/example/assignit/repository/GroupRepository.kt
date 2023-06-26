package com.example.assignit.repository

import android.util.Log
import com.example.assignit.model.Group
import com.example.assignit.model.GroupDto
import com.example.assignit.model.Invitation
import com.example.assignit.model.Task
import com.example.assignit.model.User
import com.example.assignit.util.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
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
        firebaseFirestore.collection(GROUP_COLLECTION)
            .document(group.id)
            .set(group)
    }

    /*
    fun getGroupData(groupId: String): Flow<Group?> {
        // Use firebaseFirestore to get the data for a specific group
    }

     */

    suspend fun addUserToGroup(groupId: String, user: User): Resource<Unit> {
        return try {
            // Get a reference to the group document
            val groupRef = firebaseFirestore.collection(GROUP_COLLECTION).document(groupId)

            Log.d("GroupRepository", "addUserToGroup: Path to group = ${groupRef.path}")

            // Start a transaction to add the user to the group
            firebaseFirestore.runTransaction { transaction ->
                try {
                    // Get the current state of the group using GroupDto
                    val groupDto = transaction.get(groupRef).toObject(GroupDto::class.java)

                    // Convert to Group
                    val group = groupDto?.toGroup()

                    Log.d("GroupRepository", "addUserToGroup: Group = $group")

                    if (group != null) {
                        // Add the user to the group's users list
                        group.members.add(user)
                        Log.d("GroupRepository", "addUserToGroup: Group members after addition = ${group.members}")

                        // Update the group in the database
                        // If you have GroupDto.fromGroup() function, use it here to convert group to GroupDto.
                        transaction.set(groupRef, group)
                    } else {
                        // If the group is not found, throw an exception to stop the transaction
                        throw Exception("Group not found")
                    }
                } catch (e: Exception) {
                    Log.e("GroupRepository", "Exception during transaction", e)
                    throw e
                }
            }.await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("GroupRepository", "Exception in addUserToGroup", e)
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }





    suspend fun inviteUserToGroup(groupId: String, inviterId: String, inviteeId: String): Resource<Unit> {
        return try {
            val invitation = Invitation(groupId, inviterId, inviteeId)
            firebaseFirestore.collection(INVITATION_COLLECTION)
                .document() // Generate a new document ID
                .set(invitation)
                .await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }


    companion object{
        const val GROUP_COLLECTION = "groups"
        const val INVITATION_COLLECTION = "invitations"
    }

}