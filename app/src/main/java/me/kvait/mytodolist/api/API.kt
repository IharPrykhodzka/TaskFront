package me.kvait.mytodolist.api

import me.kvait.mytodolist.data.Token
import me.kvait.mytodolist.data.dto.AuthenticationRequestDto
import me.kvait.mytodolist.data.dto.AuthenticationResponseDto
import me.kvait.mytodolist.data.dto.TaskRequestDto
import me.kvait.mytodolist.data.dto.TaskResponseDto
import retrofit2.Response
import retrofit2.http.*

interface API {

    //Authentication part

    @POST("/api/v1/registration")
    suspend fun register(@Body authenticationRequestDto: AuthenticationRequestDto): Response<Token>

    @POST("/api/v1/authentication")
    suspend fun authenticator(@Body authenticationRequestDto: AuthenticationRequestDto): Response<Token>

    //Task's part

    @GET("/api/v1/tasks")
    suspend fun getAllTasks(): List<TaskResponseDto>

    @GET("api/v1/task/{id}")
    suspend fun getTask(@Path ("id") id: Int): Response<TaskResponseDto>

    @POST("api/v1/task/save")
    suspend fun saveTask(@Body taskRequestDto: TaskRequestDto): Response<Void>

    @DELETE( "api/v1/task/{id}")
    suspend fun deleteTask(@Path ("id") id: Int): Response<Void>

    @POST("api/v1/task/update")
    suspend fun updateTask(@Body taskRequestDto: TaskRequestDto): Response<Void>
}