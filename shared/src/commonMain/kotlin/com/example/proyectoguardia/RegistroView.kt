package com.example.proyectoguardia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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

@Composable
fun RegistroView(onRegistroExitoso: () -> Unit, onVolver: () -> Unit) {
    val storage = remember { StorageService() }
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
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
            Text("Guardian Lumina", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = WarmBeige, letterSpacing = 2.sp)
            Text("Crea tu cuenta", fontSize = 16.sp, color = WarmBeige.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = WarmBeige),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Registro", fontWeight = FontWeight.Bold, color = DeepCoffee, fontSize = 20.sp)

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it; errorMessage = "" },
                        label = { Text("Nombre completo") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = DeepCoffee) },
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftAmber,
                            unfocusedBorderColor = DeepCoffee.copy(alpha = 0.3f)
                        )
                    )

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

                    OutlinedTextField(
                        value = confirmar,
                        onValueChange = { confirmar = it; errorMessage = "" },
                        label = { Text("Confirmar contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = DeepCoffee) },
                        visualTransformation = PasswordVisualTransformation(),
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
                            when {
                                nombre.isBlank() -> errorMessage = "Escribe tu nombre"
                                email.isBlank() -> errorMessage = "Escribe tu email"
                                !email.contains("@") -> errorMessage = "Email inválido"
                                password.length < 4 -> errorMessage = "Contraseña muy corta"
                                password != confirmar -> errorMessage = "Las contraseñas no coinciden"
                                else -> {
                                    storage.saveData("user_nombre", nombre)
                                    storage.saveData("user_email", email)
                                    storage.saveData("user_password", password)
                                    println("Usuario registrado: $email")
                                    onRegistroExitoso()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoftAmber)
                    ) {
                        Text("CREAR CUENTA", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    TextButton(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
                        Text("¿Ya tienes cuenta? Inicia sesión", color = DeepCoffee)
                    }
                }
            }
        }
    }
}