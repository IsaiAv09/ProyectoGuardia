package com.example.proyectoguardia

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    
    install(CORS) {
        anyHost() // Permite que tu App se conecte desde cualquier lugar
        allowHeader(HttpHeaders.ContentType)
    }

    routing {
        get("/") {
            call.respondText("¡Servidor de Guardian Lumina funcionando en Railway!")
        }

        // Aquí irán las rutas para reportar peligros o calles
        get("/estado") {
            call.respond(mapOf("status" to "ok", "mensaje" to "Base de datos conectada"))
        }
    }
}
