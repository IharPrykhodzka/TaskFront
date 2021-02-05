package me.kvait.mytodolist.data.dto

import com.google.gson.annotations.SerializedName

data class AuthenticationRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)