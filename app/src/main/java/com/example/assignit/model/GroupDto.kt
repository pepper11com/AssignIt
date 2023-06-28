package com.example.assignit.model

import com.google.firebase.Timestamp

data class GroupDto(
    val id: String = "",
    val name: String = "",
    val admin: User = User(), // Make sure `User` also has a no-argument constructor
    val members: MutableList<User> = mutableListOf(),
    val dayAndTimeEdited: Timestamp = Timestamp.now(), // use Firestore Timestamp
    val tasks: MutableList<Task> = mutableListOf(),
) {
    fun toGroup(): Group {
        // Check for invalid state and throw an exception or return null as needed
        // For example:
        if (id == "" || name == "" || admin == User()) {
            throw Exception("Invalid GroupDto state")
        }
        // You can now assume that `admin` is in `members`
        return Group(
            id = id,
            name = name,
            admin = admin,
            members = members,
            dayAndTimeEdited = dayAndTimeEdited.toDate(), // convert Timestamp to Date
            tasks = tasks
        )
    }
}

