package com.example.formvalidation.network

object RegisterApiInstance {
    val api: RegisterApi by lazy {
        UserRetrofitInstance.retrofit.create(RegisterApi::class.java)
    }
}
