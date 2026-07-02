package com.example.proyectoguardia

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyectoguardia.views.*
import com.example.proyectoguardia.basededatos.StorageService

enum class AppScreen {
    Login, Registro, Map, EmergencyContact
}

@Composable
@Preview
fun App() {
    val storage = remember { StorageService() }
    // Iniciamos la sesión si ya hay un correo guardado (sesión persistente)
    val sessionActive = remember { storage.getData("session_active") == "true" }
    var currentScreen by remember { mutableStateOf(if (sessionActive) AppScreen.Map else AppScreen.Login) }

    MaterialTheme {
        when (currentScreen) {
            AppScreen.Login -> LoginView(
                onLoginSuccess = { 
                    storage.saveData("session_active", "true")
                    currentScreen = AppScreen.Map 
                },
                onRegistro = { currentScreen = AppScreen.Registro }
            )
            AppScreen.Registro -> RegistroView(
                onRegistroExitoso = { 
                    storage.saveData("session_active", "true")
                    currentScreen = AppScreen.Map 
                },
                onVolver = { currentScreen = AppScreen.Login }
            )
            AppScreen.Map -> MainMapView(
                onLogout = { 
                    storage.saveData("session_active", "false")
                    currentScreen = AppScreen.Login 
                },
                onShowEmergencyContact = { currentScreen = AppScreen.EmergencyContact }
            )
            AppScreen.EmergencyContact -> EmergencyContactView(
                onBack = { currentScreen = AppScreen.Map }
            )
        }
    }
}
