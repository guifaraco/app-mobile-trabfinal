package com.github.guifaraco.appmobile.api

import retrofit2.Call
import retrofit2.http.*

data class LoginRequest(val username: String)
data class LoginResponse(val token: String)
data class TaskResponse(
    val id: Int,
    val data: TaskData
)

data class TaskData(
    val description: String
)

interface ApiService {
    // Requisição POST para realizar o login
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Requisição GET para obter a lista de tarefas
    @GET("identified/getData")
    fun getTasks(@Header("Authorization") token: String): Call<List<TaskResponse>>

    // Requisição POST para salvar uma tarefa
    @POST("identified/saveData")
    fun saveTask(
        @Header("Authorization") token: String,
        @Body taskData: TaskData
    ): Call<TaskResponse>
}

