package com.example.proyectoguardia

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

enum class AppScreen {
    Login, Registro, Map, EmergencyContact
}

@Composable
@Preview
fun App() {
    var currentScreen by remember { mutableStateOf(AppScreen.Login) }

    MaterialTheme {
        when (currentScreen) {
            AppScreen.Login -> LoginView(
                onLoginSuccess = { currentScreen = AppScreen.Map },
                onRegistro = { currentScreen = AppScreen.Registro }
            )
            AppScreen.Registro -> RegistroView(
                onRegistroExitoso = { currentScreen = AppScreen.Map },
                onVolver = { currentScreen = AppScreen.Login }
            )
            AppScreen.Map -> MainMapView(
                onLogout = { currentScreen = AppScreen.Login },
                onShowEmergencyContact = { currentScreen = AppScreen.EmergencyContact }
            )
            AppScreen.EmergencyContact -> EmergencyContactView(
                onBack = { currentScreen = AppScreen.Map }
            )
        }
    }
}