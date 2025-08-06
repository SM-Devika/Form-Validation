package com.example.formvalidation.model

data class Country(
    val name: Name,
    val flags: Flags,
    val capital: List<String>?,
    val region: String?,
    val population: Long?
)

data class Name(
    val common: String
)

data class Flags(
    val png: String
)
