package com.example.formvalidation.network

object LoginApiInstance {
    val api: LoginApi by lazy {
        UserRetrofitInstance.retrofit.create(LoginApi::class.java)
    }
}

