package com.example.proyectoguardia.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoguardia.basededatos.ApiService
import com.example.proyectoguardia.basededatos.StorageService
import com.example.proyectoguardia.modelos.EmergencyContact
import kotlinx.coroutines.launch

@Composable
fun EmergencyContactView(
    onBack: () -> Unit
) {
    val storage = StorageService()
    val apiService = ApiService()
    val scope = rememberCoroutineScope()
    
    // Carga inicial de datos
    fun getSaved(key: String) = storage.getData(key).let { if (it == "Sin datos") "" else it }

    var nombre by remember { mutableStateOf(getSaved("contacto_nombre")) }
    var numero by remember { mutableStateOf(getSaved("contacto_numero")) }
    var parentesco by remember { mutableStateOf(getSaved("contacto_parentesco")) }
    
    var isEditable by remember { mutableStateOf(nombre.isEmpty()) }
    
    // Estados para el resumen (lo que está guardado físicamente)
    var savedNombre by remember { mutableStateOf(getSaved("contacto_nombre")) }
    var savedNumero by remember { mutableStateOf(getSaved("contacto_numero")) }
    var savedParentesco by remember { mutableStateOf(getSaved("contacto_parentesco")) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBeige)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "Contacto de Emergencia",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DeepCoffee,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CozyCream),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { if (isEditable) nombre = it },
                        label = { Text("Nombre") },
                        enabled = isEditable,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftAmber,
                            unfocusedBorderColor = DeepCoffee.copy(alpha = 0.3f),
                            disabledBorderColor = DeepCoffee.copy(alpha = 0.1f)
                        )
                    )

                    OutlinedTextField(
                        value = numero,
                        onValueChange = { if (isEditable) numero = it },
                        label = { Text("Número telefónico") },
                        enabled = isEditable,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftAmber,
                            unfocusedBorderColor = DeepCoffee.copy(alpha = 0.3f),
                            disabledBorderColor = DeepCoffee.copy(alpha = 0.1f)
                        )
                    )

                    OutlinedTextField(
                        value = parentesco,
                        onValueChange = { if (isEditable) parentesco = it },
                        label = { Text("Parentesco") },
                        enabled = isEditable,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftAmber,
                            unfocusedBorderColor = DeepCoffee.copy(alpha = 0.3f),
                            disabledBorderColor = DeepCoffee.copy(alpha = 0.1f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botones de Acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isEditable) {
                    Button(
                        onClick = {
                            // Filtro de seguridad implementado en EmergencyContactView.kt
                            if (numero.length == 10) {
                                val lada = numero.substring(0, 3)
                                println("DEPURACIÓN: Procesando Lada: $lada")

                                // 1. Guardar localmente
                                storage.saveData("contacto_nombre", nombre)
                                storage.saveData("contacto_numero", numero)
                                storage.saveData("contacto_parentesco", parentesco)
                                savedNombre = nombre
                                savedNumero = numero
                                savedParentesco = parentesco
                                isEditable = false

                                // 2. Enviar al servidor
                                val contacto = EmergencyContact(nombre, numero, parentesco)
                                scope.launch {
                                    apiService.guardarContacto(contacto)
                                }
                            } else {
                                println("DEPURACIÓN: Datos inválidos. Se evitó la excepción.")
                            }
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoftAmber),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Guardar", color = Color.White)
                    }
                } else {
                    Button(
                        onClick = { isEditable = true },
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepCoffee),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Editar", color = Color.White)
                    }
                }

                Button(
                    onClick = {
                        storage.saveData("contacto_nombre", "")
                        storage.saveData("contacto_numero", "")
                        storage.saveData("contacto_parentesco", "")
                        nombre = ""
                        numero = ""
                        parentesco = ""
                        savedNombre = ""
                        savedNumero = ""
                        savedParentesco = ""
                        isEditable = true
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Eliminar", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Resumen Visual
            if (savedNombre.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Contacto guardado:", fontWeight = FontWeight.Bold, color = DeepCoffee)
                        Text(savedNombre, fontSize = 18.sp, color = DeepCoffee)
                        Text(savedParentesco, fontSize = 14.sp, color = Color.Gray)
                        Text(savedNumero, fontSize = 16.sp, color = SoftAmber, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = onBack,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Volver", color = DeepCoffee, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
