package com.example.proyectoguardia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMapView(onLogout: () -> Unit) {
    var isMapLoading by remember { mutableStateOf(true) }
    var isReportingMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("LUMINA", fontWeight = FontWeight.ExtraBold, color = DeepCoffee, fontSize = 20.sp)
                        Text("Tulancingo, Hidalgo", fontSize = 12.sp, color = SoftAmber)
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, null, tint = DeepCoffee)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmBeige)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            MapView(
                modifier = Modifier.fillMaxSize(),
                isReportingMode = isReportingMode,
                onPageFinished = { isMapLoading = false }
            )
            
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
            ) {
                if (!isReportingMode) {
                    ExtendedFloatingActionButton(
                        onClick = { isReportingMode = true },
                        icon = { Icon(Icons.Default.AddLocationAlt, null) },
                        text = { Text("TRAZAR CALLE ILUMINADA") },
                        containerColor = SoftAmber,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    )
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = WarmBeige),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = SoftAmber,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Lightbulb, null, tint = Color.White, modifier = Modifier.size(24.dp))
                                    }
                                }
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text("Selecciona 2 puntos", color = DeepCoffee, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("Toca el inicio y el fin", color = DeepCoffee.copy(alpha = 0.6f), fontSize = 12.sp)
                                }
                            }
                            IconButton(onClick = { isReportingMode = false }) {
                                Icon(Icons.Default.Close, null, tint = DeepCoffee)
                            }
                        }
                    }
                }
            }

            if (isMapLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(WarmBeige),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SoftAmber)
                }
            }
        }
    }
}
