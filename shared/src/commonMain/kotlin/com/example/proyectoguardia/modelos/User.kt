package com.example.proyectoguardia.modelos

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val nombre: String? = null,
    val email: String,
    val password: String
)
