package me.kvait.mytodolist.data.dto

import com.google.gson.annotations.SerializedName

data class AuthenticationRequestDto(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)