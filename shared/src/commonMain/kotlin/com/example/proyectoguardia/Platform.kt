package com.example.proyectoguardia

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform