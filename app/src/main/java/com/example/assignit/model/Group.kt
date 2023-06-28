package com.example.assignit.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Group(
    val id: String,
    val name: String,
    val admin: User, // New field for admin user
    val members: List<User> = listOf(admin), // Changed to List
    val dayAndTimeEdited: Date = Date(),
    val tasks: List<Task> = listOf(), // Changed to List
) {
    fun getFormattedDate(): String {
        val format = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault())
        return format.format(dayAndTimeEdited)
    }
    //dayAndTimeEdited is the date and time when the last change was made to the group (e.g. a task was added or removed)
    fun addTask(task: Task): Group {
        return this.copy(tasks = tasks + task,
            dayAndTimeEdited = Date())
    }

    fun removeTask(task: Task): Group {
        return this.copy(tasks = tasks - task,
            dayAndTimeEdited = Date())
    }

    fun addMember(user: User): Group {
        return this.copy(members = members + user,
            dayAndTimeEdited = Date())
    }

    fun removeMember(user: User): Group {
        return this.copy(members = members - user,
            dayAndTimeEdited = Date())
    }

    fun changeName(newName: String): Group {
        return this.copy(name = newName,
            dayAndTimeEdited = Date())
    }

    fun changeAdmin(newAdmin: User): Group {
        return this.copy(admin = newAdmin,
            dayAndTimeEdited = Date())
    }

}