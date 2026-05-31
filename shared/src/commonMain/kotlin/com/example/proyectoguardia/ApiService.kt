package com.example.proyectoguardia

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class ApiService {
    private val client = HttpClient()

    suspend fun fetchData(): String {
        return try {
            val response = client.get("https://jsonplaceholder.typicode.com/todos/1")
            response.bodyAsText()
        } catch (e: Exception) {
            "Error en la conexión"
        }
    }
}