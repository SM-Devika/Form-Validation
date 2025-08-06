package com.example.formvalidation.viewmodel

import com.example.formvalidation.network.CountryRetrofitInstance
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formvalidation.model.Country
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CountryViewModel : ViewModel() {

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> = _countries

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        viewModelScope.launch {
            try {
                val response = CountryRetrofitInstance.api.getCountries()
                println("Fetched countries : ${response.size}")
                _countries.value = response
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load countries: ${e.message}"
                println("Error fetching countries: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
