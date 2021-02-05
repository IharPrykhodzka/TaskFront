package me.kvait.mytodolist.data.dto

import me.kvait.mytodolist.data.model.TaskModel

class TaskResponseDto(
    val id: Int,
    val user_id: Int,
    val title: String,
    val content: String,
    val createdDate: String
) {
    companion object {
        fun TaskResponseDto.toModel() = TaskModel(
            id = this.id,
            user_id = this.user_id,
            title = this.title,
            content = this.content,
            createdDate = this.createdDate
        )
    }
}