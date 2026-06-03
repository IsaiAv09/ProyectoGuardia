package com.example.proyectoguardia

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

enum class AppScreen {
    Login, Map
}

@Composable
@Preview
fun App() {
    var currentScreen by remember { mutableStateOf(AppScreen.Login) }

    MaterialTheme {
        when (currentScreen) {
            AppScreen.Login -> LoginView(
                onLoginSuccess = { currentScreen = AppScreen.Map }
            )
            AppScreen.Map -> MainMapView(
                onLogout = { currentScreen = AppScreen.Login }
            )
        }
    }
}
