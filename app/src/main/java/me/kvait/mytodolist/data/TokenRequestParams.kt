package me.kvait.mytodolist.data

import com.google.gson.annotations.SerializedName

data class TokenRequestParams(
        @SerializedName("token")
        val token: String
)
