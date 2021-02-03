package me.kvait.mytodolist.api

import me.kvait.mytodolist.data.dto.AuthenticationRequestDto
import me.kvait.mytodolist.data.dto.TaskRequestDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiRepository {

    // Ленивое создание Retrofit экземпляра
    private var retrofit: Retrofit =
            Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8090")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient().newBuilder()
                            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .connectTimeout(1, TimeUnit.MINUTES)
                            .writeTimeout(1, TimeUnit.MINUTES)
                            .readTimeout(1, TimeUnit.MINUTES)
                            .build()
                    ).build()

    // Ленивое создание API
    private var myAPI: API = retrofit.create(me.kvait.mytodolist.api.API::class.java)

    //Authentication with token
    fun createRetrofitWithAuth(authToken: String) {

        retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8090")
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                        OkHttpClient().newBuilder()
                                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                                .addInterceptor(InjectAuthTokenInterceptor(authToken))
                                .connectTimeout(1, TimeUnit.MINUTES)
                                .writeTimeout(1, TimeUnit.MINUTES)
                                .readTimeout(1, TimeUnit.MINUTES)
                                .build()
                )
                .build()

        myAPI = retrofit.create(me.kvait.mytodolist.api.API::class.java)

    }

    //Authentication part
    suspend fun authentication(login: String, password: String) =
            myAPI.authenticator(AuthenticationRequestDto(login, password))

    suspend fun registration(login: String, password: String) =
            myAPI.register(AuthenticationRequestDto(login, password))


    //Task's part
    suspend fun mGetAllTasks() =
            myAPI.getAllTasks()

    suspend fun mGetTaskById(taskId: Int) =
            myAPI.getTask(taskId)

    suspend fun mDeleteTaskById(taskId: Int) =
            myAPI.deleteTask(taskId)

    suspend fun mSaveTask(taskRequestDto: TaskRequestDto) =
            myAPI.saveTask(taskRequestDto)

    suspend fun mUpdateTaskById(taskRequestDto: TaskRequestDto) =
            myAPI.updateTask(taskRequestDto)
}