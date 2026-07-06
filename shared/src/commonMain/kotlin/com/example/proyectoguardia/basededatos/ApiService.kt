package com.example.proyectoguardia.basededatos

import com.example.proyectoguardia.modelos.EmergencyContact
import com.example.proyectoguardia.modelos.User
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiService {

    // Cliente HTTP configurado para Railway
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
            socketTimeoutMillis = 15000
        }
    }


    suspend fun guardarContacto(contacto: EmergencyContact): Boolean {
        return try {
            println("--- INICIO DE PETICIÓN POST A RAILWAY ---")

            val response = client.post("https://guardian-backend-production-94d5.up.railway.app/index.php") {
                contentType(ContentType.Application.Json)
                setBody(contacto)
            }
            
            val responseText = response.bodyAsText()
            println("Status de Railway: ${response.status}")
            println("Respuesta de Railway: $responseText")
            
            response.status.isSuccess()
        } catch (e: Exception) {
            println("Error de conexión a Railway: ${e.message}")
            false
        }
    }


    suspend fun eliminarContacto(numero: String): Boolean {
        return try {
            println("--- INICIO DE PETICIÓN DELETE A RAILWAY ---")
            val response = client.delete("https://guardian-backend-production-94d5.up.railway.app/index.php") {
                parameter("numero", numero)
            }
            println("Status de Railway (Delete): ${response.status}")
            response.status.isSuccess()
        } catch (e: Exception) {
            println("Error al eliminar en Railway: ${e.message}")
            false
        }
    }

    suspend fun registrarUsuario(user: User): Boolean {
        return try {
            val response = client.post("https://guardian-backend-production-94d5.up.railway.app/registro") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun iniciarSesion(email: String, pass: String): Boolean {
        return try {
            val response = client.post("https://guardian-backend-production-94d5.up.railway.app/login") {
                contentType(ContentType.Application.Json)
                setBody(User(email = email, password = pass))
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            false
        }
    }
}
