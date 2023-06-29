package com.example.assignit.model

import com.google.firebase.Timestamp

data class GroupDto(
    val id: String = "",
    val name: String = "",
    val adminId: String = "",
    val memberIds: MutableList<String> = mutableListOf(),
    val dayAndTimeEdited: Timestamp = Timestamp.now(),
    val taskIds: MutableList<String> = mutableListOf(),
) {
    fun toGroup(): Group {
        if (id == "" || name == "" || adminId == "") {
            throw Exception("Invalid GroupDto state")
        }
        return Group(
            id = id,
            name = name,
            adminId = adminId,
            memberIds = memberIds,
            dayAndTimeEdited = dayAndTimeEdited.toDate(),
            taskIds = taskIds
        )
    }
}

