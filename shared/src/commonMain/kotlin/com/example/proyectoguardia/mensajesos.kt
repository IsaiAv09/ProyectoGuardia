// Vista por rama2 - Botón de seguridad
package com.example.proyectoguardia

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SOSScreen() {
    var progress by remember { mutableStateOf(0f) }
    var buttonColor by remember { mutableStateOf(Color(0xFFD93025)) }
    var buttonText by remember { mutableStateOf("MANTÉN PRESIONADO \n PARA ENVIAR \n SOS") }
    var isSent by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(durationMillis = 1500))
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFE5E5E5)).padding(24.dp)
    ) {
        // Superior
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Row {
                Text("Proyecto Guardia ", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                Text("SOS", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD93025))
            }
            Text("EMERGENCIA ACTIVADA", fontSize = 14.sp, color = Color.Gray)
        }

        // Centro
        Box(modifier = Modifier.align(Alignment.Center), contentAlignment = Alignment.Center) {
            // Carga
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.size(260.dp),
                color = if (isSent) Color(0xFF1E8E3E) else Color(0xFFD93025),
                strokeWidth = 8.dp,
                trackColor = Color.LightGray.copy(alpha = 0.3f),
                strokeCap = StrokeCap.Round,
            )

            // Botón
            Surface(
                modifier = Modifier.size(220.dp).pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            if (!isSent) {
                                val job = scope.launch {
                                    progress = 1f
                                    delay(1500)
                                    isSent = true
                                    buttonColor = Color(0xFF1E8E3E)
                                    buttonText = "¡MENSAJE ENVIADO!"
                                }
                                try { awaitRelease() } finally { 
                                    if (!isSent) {
                                        job.cancel()
                                        progress = 0f 
                                    }
                                }
                            }
                        }
                    )
                },
                shape = CircleShape,
                color = buttonColor,
                shadowElevation = 12.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(buttonText, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            }
        }

        // Inferior
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Av. Reforma 222 (Aprox.)", fontSize = 12.sp, color = Color.Gray)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚠️", fontSize = 14.sp)
                Text(" Sin internet - Modo SMS Activo", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
