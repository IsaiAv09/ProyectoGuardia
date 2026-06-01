package com.example.proyectoguardia

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/// Vista realizada por Main

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardiaView() {
    val guardiaService = remember { GuardiaService() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var apiResult by remember { mutableStateOf("") }
    var localData by remember { mutableStateOf("No hay datos") }
    var statusMessage by remember { mutableStateOf("Listo para comenzar") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Proyecto Guardia") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección de Explicación
            InfoCard(
                title = "Requisitos del Proyecto",
                description = "Esta aplicación cumple con:\n" +
                        "1. Una vista (esta pantalla).\n" +
                        "2. Un servicio simple (GuardiaService).\n" +
                        "3. Consumo de API (Ktor).\n" +
                        "4. Almacenamiento local (Settings).",
                icon = Icons.Default.Info
            )

            // Sección de API
            ActionCard(
                title = "Consumo de API (Ktor)",
                description = "Presiona el botón para obtener datos en tiempo real de una API REST.",
                buttonText = "Obtener Datos API",
                icon = Icons.Default.Refresh,
                isLoading = isLoading,
                onClick = {
                    scope.launch {
                        isLoading = true
                        statusMessage = "Conectando con la API..."
                        val data = guardiaService.getAndSaveData()
                        apiResult = data
                        statusMessage = "¡Datos recibidos y guardados!"
                        isLoading = false
                    }
                }
            )

            if (apiResult.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Resultado API:", fontWeight = FontWeight.Bold)
                        Text(apiResult, fontSize = 12.sp)
                    }
                }
            }

            // Sección de Almacenamiento Local
            ActionCard(
                title = "Almacenamiento Local",
                description = "Recupera el último dato guardado en el dispositivo (Multiplatform Settings).",
                buttonText = "Cargar de Local",
                icon = Icons.Default.Storage,
                onClick = {
                    val data = guardiaService.getLocalData()
                    localData = data
                    statusMessage = "Datos cargados desde el almacenamiento local."
                }
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Dato en Local:", fontWeight = FontWeight.Bold)
                    Text(localData)
                }
            }

            // Mensaje de estado al final
            Text(
                text = "Estado: $statusMessage",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun InfoCard(title: String, description: String, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(text = description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    description: String,
    buttonText: String,
    icon: ImageVector,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onClick,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(buttonText)
                }
            }
        }
    }
}
