package me.kvait.mytodolist.data.dto

import com.google.gson.annotations.SerializedName

class TaskRequestDto(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("content")
        val content: String,
        @SerializedName("createdDate")
        val createdDate: String
)