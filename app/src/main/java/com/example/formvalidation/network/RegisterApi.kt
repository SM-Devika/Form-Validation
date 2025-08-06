package com.example.formvalidation.network

import com.example.formvalidation.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RegisterApi {

    @POST("register")
    suspend fun registerUser(@Body user: User): Response<User>

    @GET("register")
    suspend fun getUsers(): Response<List<User>>
}
