package com.example.proyectoguardia.basededatos

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.example.proyectoguardia.EmergencyContact

class ApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    suspend fun guardarContacto(contacto: EmergencyContact) {
        try {
            // Log para debug como se menciona en EmergencyContactView
            println("ApiService: Enviando contacto al servidor: $contacto")
            
            // Ejemplo de POST (comentado ya que no hay una URL real proporcionada)
            /*
            client.post("https://tu-api-aqui.com/api/contacto") {
                contentType(ContentType.Application.Json)
                setBody(contacto)
            }
            */
        } catch (e: Exception) {
            println("ApiService: Error al guardar contacto: ${e.message}")
        }
    }
}
