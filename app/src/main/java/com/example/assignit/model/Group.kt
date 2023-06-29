package com.example.assignit.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Group(
    val id: String,
    val name: String,
    val adminId: String,
    val memberIds: List<String> = listOf(adminId),
    val dayAndTimeEdited: Date = Date(),
    val taskIds: List<String> = listOf(),
) {
    fun getFormattedDate(): String {
        val format = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault())
        return format.format(dayAndTimeEdited)
    }

    fun addTask(taskId: String): Group {
        return this.copy(taskIds = taskIds + taskId,
            dayAndTimeEdited = Date())
    }

    fun removeTask(taskId: String): Group {
        return this.copy(taskIds = taskIds - taskId,
            dayAndTimeEdited = Date())
    }

    fun addMember(userId: String): Group {
        return this.copy(memberIds = memberIds + userId,
            dayAndTimeEdited = Date())
    }

    fun removeMember(userId: String): Group {
        return this.copy(memberIds = memberIds - userId,
            dayAndTimeEdited = Date())
    }

    fun changeName(newName: String): Group {
        return this.copy(name = newName,
            dayAndTimeEdited = Date())
    }

    fun changeAdmin(newAdminId: String): Group {
        return this.copy(adminId = newAdminId,
            dayAndTimeEdited = Date())
    }
}