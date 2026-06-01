package com.example.proyectoguardia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LuminaApp() {
    // Ya no necesitamos controlar pantallas, siempre mostramos el Mapa
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        MapaTulancingoScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaTulancingoScreen() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    var isMapLoading by remember { mutableStateOf(true) }
    
    val comentarios = remember { mutableStateListOf<String>() }
    var nuevoComentario by remember { mutableStateOf("") }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 120.dp,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(Modifier.size(40.dp, 4.dp).background(Color.LightGray, CircleShape))
                Spacer(Modifier.height(16.dp))

                Text("Marcar en el mapa:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SecurityIconButton(Icons.Default.Videocam, "Cámara", Color.Blue)
                    SecurityIconButton(Icons.Default.Lightbulb, "Luz", Color.Yellow)
                    SecurityIconButton(Icons.Default.Shield, "Policía", Color.Green)
                    SecurityIconButton(Icons.Default.Place, "Peligro", Color.Red)
                }

                HorizontalDivider()

                Spacer(Modifier.height(16.dp))
                Text("Comentarios de la zona (Tulancingo)", fontWeight = FontWeight.Bold)
                
                OutlinedTextField(
                    value = nuevoComentario,
                    onValueChange = { nuevoComentario = it },
                    placeholder = { Text("Escribe un reporte...") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            if (nuevoComentario.isNotBlank()) {
                                comentarios.add(0, nuevoComentario)
                                nuevoComentario = ""
                            }
                        }) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Color(0xFF1976D2))
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.height(200.dp).fillMaxWidth()) {
                    items(comentarios) { comentario ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                        ) {
                            Text(comentario, modifier = Modifier.padding(12.dp), fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            MapView(modifier = Modifier.fillMaxSize(), onPageFinished = { isMapLoading = false })
            
            // BOTÓN SOS: Ahora es solo visual, no navega a ningún lado
            FloatingActionButton(
                onClick = { /* No hace nada, se queda en el mapa */ },
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                containerColor = Color.Red,
                contentColor = Color.White
            ) {
                Text("SOS", fontWeight = FontWeight.Bold)
            }

            if (isMapLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun SecurityIconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { }, modifier = Modifier.background(color.copy(alpha = 0.1f), CircleShape)) {
            Icon(icon, contentDescription = null, tint = color)
        }
        Text(label, fontSize = 10.sp, color = Color.Gray)
    }
}
