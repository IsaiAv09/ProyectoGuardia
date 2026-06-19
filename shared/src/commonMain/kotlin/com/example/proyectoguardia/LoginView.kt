package com.example.proyectoguardia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoguardia.basededatos.StorageService

val WarmBeige = Color(0xFFFDF5E6)
val SoftAmber = Color(0xFFFFB74D)
val DeepCoffee = Color(0xFF4E342E)
val CozyCream = Color(0xFFFFFDE7)

@Composable
fun LoginView(onLoginSuccess: () -> Unit, onRegistro: () -> Unit) {
    val storage = remember { StorageService() }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF8D6E63), Color(0xFF4E342E))
    )

    Box(
        modifier = Modifier.fillMaxSize().background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {
            Surface(
                modifier = Modifier.size(90.dp),
                shape = RoundedCornerShape(30.dp),
                color = SoftAmber,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Lightbulb, null, tint = Color.White, modifier = Modifier.size(45.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Guardian Lumina", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = WarmBeige, letterSpacing = 2.sp)
            Text("Iluminando tu camino", fontSize = 16.sp, color = WarmBeige.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = WarmBeige),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Bienvenido de nuevo", fontWeight = FontWeight.Bold, color = DeepCoffee, fontSize = 20.sp)

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; errorMessage = "" },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = DeepCoffee) },
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftAmber,
                            unfocusedBorderColor = DeepCoffee.copy(alpha = 0.3f)
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = "" },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = DeepCoffee) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    null, tint = DeepCoffee
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftAmber,
                            unfocusedBorderColor = DeepCoffee.copy(alpha = 0.3f)
                        )
                    )

                    if (errorMessage.isNotEmpty()) {
                        Text(errorMessage, color = Color(0xFFD32F2F), fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            val emailGuardado = storage.getData("user_email")
                            val passGuardado = storage.getData("user_password")
                            if ((email == "lumina@gmail.com" && password == "1234") ||
                                (email == emailGuardado && password == passGuardado && emailGuardado != "Sin datos")
                            ) {
                                onLoginSuccess()
                            } else {
                                errorMessage = "Credenciales incorrectas"
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoftAmber)
                    ) {
                        Text("ENTRAR", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    TextButton(
                        onClick = onRegistro,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("¿No tienes cuenta? Regístrate", color = DeepCoffee)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("lumina@gmail.com / 1234", color = WarmBeige.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}