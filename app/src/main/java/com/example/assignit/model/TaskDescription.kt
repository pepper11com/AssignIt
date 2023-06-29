package com.example.assignit.model

data class TaskDescription(
    val id: String,
    val description: String,
    var done: Boolean = false,
)

data class TaskDescriptionDto(
    val id: String = "",
    val description: String = "",
    var done: Boolean = false,
) {
    fun toTaskDescription(): TaskDescription {
        return TaskDescription(
            id = id,
            description = description,
            done = done,
        )
    }
}


