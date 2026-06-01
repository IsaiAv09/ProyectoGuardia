package com.example.proyectoguardia

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

enum class Screen {
    Mapa, SOS, Guardia
}

@Composable
@Preview
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Mapa) }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Map, contentDescription = "Mapa") },
                        label = { Text("Mapa") },
                        selected = currentScreen == Screen.Mapa,
                        onClick = { currentScreen = Screen.Mapa }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Warning, contentDescription = "SOS") },
                        label = { Text("SOS") },
                        selected = currentScreen == Screen.SOS,
                        onClick = { currentScreen = Screen.SOS }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Info, contentDescription = "Info") },
                        label = { Text("Info") },
                        selected = currentScreen == Screen.Guardia,
                        onClick = { currentScreen = Screen.Guardia }
                    )
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                when (currentScreen) {
                    Screen.Mapa -> MapaTulancingoScreen()
                    Screen.SOS -> SOSScreen()
                    Screen.Guardia -> GuardiaView()
                }
            }
        }
    }
}
