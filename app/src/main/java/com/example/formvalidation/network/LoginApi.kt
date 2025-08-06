package com.example.formvalidation.network

import com.example.formvalidation.model.User
import retrofit2.http.GET

interface LoginApi {
    @GET("register")
    suspend fun getAllUsers(): List<User>
}

