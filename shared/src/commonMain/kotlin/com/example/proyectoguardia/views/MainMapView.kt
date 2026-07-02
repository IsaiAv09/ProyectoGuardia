package com.example.proyectoguardia.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoguardia.componentes.MapView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMapView(onLogout: () -> Unit, onShowEmergencyContact: () -> Unit) {
    var isMapLoading by remember { mutableStateOf(true) }
    var isReportingMode by remember { mutableStateOf(false) }
    var isPlacementActive by remember { mutableStateOf(false) }
    var reportType by remember { mutableStateOf("light") } // "light", "store", "people", "danger"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Guardian Lumina", fontWeight = FontWeight.ExtraBold, color = DeepCoffee, fontSize = 20.sp)
                        Text("Tulancingo, Hidalgo", fontSize = 12.sp, color = SoftAmber)
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, null, tint = DeepCoffee)
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
                isPlacementActive = isPlacementActive,
                reportType = reportType,
                onPageFinished = { isMapLoading = false }
            )

            SmallFloatingActionButton(
                onClick = onShowEmergencyContact,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                containerColor = SoftAmber.copy(alpha = 0.9f),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Phone, "Emergencia", modifier = Modifier.size(20.dp))
            }
            
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!isReportingMode) {
                    ExtendedFloatingActionButton(
                        onClick = { isReportingMode = true },
                        icon = { Icon(Icons.Default.AddLocationAlt, null) },
                        text = { Text("REPORTAR EN EL MAPA") },
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
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Gestión de Reportes", color = DeepCoffee, fontWeight = FontWeight.Bold)
                                IconButton(onClick = { 
                                    isReportingMode = false
                                    isPlacementActive = false
                                }) {
                                    Icon(Icons.Default.Close, null, tint = DeepCoffee)
                                }
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                ReportTypeItem("Luz", "light", reportType == "light" && isPlacementActive, Color(0xFFFFD54F), Icons.Default.Lightbulb) { 
                                    if (reportType == "light" && isPlacementActive) isPlacementActive = false
                                    else { reportType = "light"; isPlacementActive = true }
                                }
                                ReportTypeItem("Tienda", "store", reportType == "store" && isPlacementActive, Color(0xFF4CAF50), Icons.Default.Store) { 
                                    if (reportType == "store" && isPlacementActive) isPlacementActive = false
                                    else { reportType = "store"; isPlacementActive = true }
                                }
                                ReportTypeItem("Gente", "people", reportType == "people" && isPlacementActive, Color(0xFF2196F3), Icons.Default.Groups) { 
                                    if (reportType == "people" && isPlacementActive) isPlacementActive = false
                                    else { reportType = "people"; isPlacementActive = true }
                                }
                                ReportTypeItem("Peligro", "danger", reportType == "danger" && isPlacementActive, Color(0xFFF44336), Icons.Default.Warning) { 
                                    if (reportType == "danger" && isPlacementActive) isPlacementActive = false
                                    else { reportType = "danger"; isPlacementActive = true }
                                }
                            }
                            
                            Spacer(Modifier.height(8.dp))
                            
                            if (isPlacementActive) {
                                Text(
                                    text = when(reportType) {
                                        "light" -> "📍 MODO COLOCACIÓN: Toca 2 puntos"
                                        "store" -> "📍 MODO COLOCACIÓN: Toca el mapa"
                                        "people" -> "📍 MODO COLOCACIÓN: Toca el mapa"
                                        else -> "📍 MODO COLOCACIÓN: ZONA PELIGROSA"
                                    },
                                    fontSize = 12.sp,
                                    color = Color(0xFFF44336),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Toca de nuevo el icono arriba para salir",
                                    fontSize = 10.sp,
                                    color = DeepCoffee.copy(alpha = 0.6f)
                                )
                            } else {
                                Text(
                                    "💡 TIP: Selecciona un icono arriba para colocar\nO toca un reporte existente para BORRAR",
                                    fontSize = 11.sp,
                                    color = SoftAmber,
                                    fontWeight = FontWeight.SemiBold
                                )
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

@Composable
fun ReportTypeItem(label: String, type: String, isSelected: Boolean, color: Color, icon: ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = if (isSelected) color else color.copy(alpha = 0.1f),
            border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, color) else null,
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) Color.White else color,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) color else DeepCoffee.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
