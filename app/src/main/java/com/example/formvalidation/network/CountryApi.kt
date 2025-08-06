package com.example.formvalidation.network

import com.example.formvalidation.model.Country
import retrofit2.http.GET

interface CountryApi {
    @GET("all?fields=name,flags,capital,region,population")
    suspend fun getCountries(): List<Country>
}
