package com.example.proyectoguardia

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiService {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun fetchData(): String {
        return try {
            val response = client.get("https://jsonplaceholder.typicode.com/todos/1")
            response.bodyAsText()
        } catch (e: Exception) {
            "Error en la conexión: ${e.message}"
        }
    }

    suspend fun guardarContacto(contacto: EmergencyContact): Boolean {
        return try {
            println("--- INICIO DE PETICIÓN POST ---")
            println("Enviando contacto...")
            println("Datos: $contacto")
            
            val response = client.post("https://jsonplaceholder.typicode.com/posts") {
                contentType(ContentType.Application.Json)
                setBody(contacto)
            }
            
            println("Endpoint ejecutado")
            println("Respuesta del servidor: ${response.bodyAsText()}")
            println("--- FIN DE PETICIÓN (EXITOSO) ---")
            true
        } catch (e: Exception) {
            println("--- ERROR EN ENDPOINT ---")
            println("Error endpoint: ${e.message}")
            println("Causa: ${e.cause}")
            println("--- FIN DE PETICIÓN (FALLIDO) ---")
            false
        }
    }
}
