package com.example.assignit.model

import com.google.firebase.Timestamp
import java.util.Date

data class Task(
    val id: String,
    val groupId: String,
    val title: String,
    val descriptions: List<TaskDescription>,
    val assigneeIds: List<String>,
    val dueDate: Date,
)

data class TaskDto(
    val id: String = "",
    val groupId: String = "",
    val title: String = "",
    val descriptions: MutableList<TaskDescriptionDto> = mutableListOf(),
    val assigneeIds: MutableList<String> = mutableListOf(),
    val dueDate: Timestamp = Timestamp.now(),
) {
    fun toTask(): Task {
        return Task(
            id = id,
            groupId = groupId,
            title = title,
            descriptions = descriptions.map { it.toTaskDescription() },
            assigneeIds = assigneeIds,
            dueDate = dueDate.toDate(),
        )
    }
}
