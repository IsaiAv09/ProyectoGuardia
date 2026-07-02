package com.example.proyectoguardia.modelos

import kotlinx.serialization.Serializable

/**
 * MODELO DE DATOS: Contacto de Emergencia
 * Representa la estructura de información de un contacto guardado.
 */
@Serializable
data class EmergencyContact(
    val nombre: String,
    val numero: String,
    val parentesco: String
)
