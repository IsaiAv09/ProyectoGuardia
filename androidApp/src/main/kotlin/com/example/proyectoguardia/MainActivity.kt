package com.example.proyectoguardia

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyectoguardia.servicios.androidContext
import com.example.proyectoguardia.servicios.LuminaForegroundService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        // Inicializamos el contexto para el servicio de notificaciones
        androidContext = this

        // Iniciamos el servicio de primer plano
        val serviceIntent = Intent(this, LuminaForegroundService::class.java)
        startService(serviceIntent)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

