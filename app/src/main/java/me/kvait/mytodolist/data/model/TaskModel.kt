package me.kvait.mytodolist.data.model

import java.io.Serializable

data class TaskModel(
        val id: Int,
        val user_id: Int,
        val title: String,
        val content: String,
        val createdDate: String
) : Serializable