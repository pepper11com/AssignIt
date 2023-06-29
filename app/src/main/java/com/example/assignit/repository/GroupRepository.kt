package com.example.assignit.repository

import android.util.Log
import com.example.assignit.model.Group
import com.example.assignit.model.GroupDto
import com.example.assignit.model.Invitation
import com.example.assignit.model.User
import com.example.assignit.util.resource.Resource
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
){

    suspend fun createGroup(group: Group) {
        // Convert Group to GroupDto
        val groupDto = GroupDto(
            id = group.id,
            name = group.name,
            adminId = group.adminId,
            memberIds = group.memberIds.toMutableList(),
            dayAndTimeEdited = Timestamp.now(),
            taskIds = group.taskIds.toMutableList()
        )

        // Add groupDto to Firestore
        firebaseFirestore.collection(GROUP_COLLECTION)
            .document(groupDto.id)
            .set(groupDto)

        // Add group ID to the admin's list of groups
        val userRef = firebaseFirestore.collection(USER_COLLECTION).document(group.adminId)
        userRef.get().addOnSuccessListener { document ->
            val user = document.toObject(User::class.java)
            if (user != null) {
                val updatedUser = user.copy(groups = user.groups + groupDto.id)
                userRef.set(updatedUser)
            }
        }
    }

    suspend fun getGroup(groupId: String): Resource<Group> {
        return try {
            val groupDto = firebaseFirestore.collection(GROUP_COLLECTION)
                .document(groupId)
                .get()
                .await()
                .toObject(GroupDto::class.java)!!

            val group = groupDto.toGroup()
            Log.d("GroupRepository", "getGroup: $group")
            Resource.Success(group)
        } catch (e: Exception) {
            Log.e("GroupRepository", "Error getting group", e)
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }




    suspend fun addUserToGroup(groupId: String, user: User): Resource<Unit> {
        return try {
            // Get a reference to the group document
            val groupRef = firebaseFirestore.collection(GROUP_COLLECTION).document(groupId)

            // Get a reference to the user document
            val userRef = firebaseFirestore.collection(USER_COLLECTION).document(user.id!!)

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
                        // Check if user is already a member or an admin of the group
                        if (group.memberIds.contains(user.id) || group.adminId == user.id) {
                            return@runTransaction
                        }

                        // Add the user to the group's users list and get the updated group
                        val updatedGroup = group.addMember(user.id!!)
                        Log.d("GroupRepository", "addUserToGroup: Group members after addition = ${updatedGroup.memberIds}")

                        // Update the group in the database
                        val updatedGroupDto = GroupDto(
                            id = updatedGroup.id,
                            name = updatedGroup.name,
                            adminId = updatedGroup.adminId,
                            memberIds = updatedGroup.memberIds.toMutableList(),
                            dayAndTimeEdited = Timestamp.now(),
                            taskIds = updatedGroup.taskIds.toMutableList()
                        )
                        transaction.set(groupRef, updatedGroupDto)

                        // Update user's group list
                        val updatedUserGroups = (user.groups + groupId).toSet().toList()
                        val updatedUser = user.copy(groups = updatedUserGroups)
                        transaction.set(userRef, updatedUser)
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
        private const val USER_COLLECTION = "users"
    }
}