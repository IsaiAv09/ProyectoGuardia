package com.example.proyectoguardia.servicios

/**
 * INTERFAZ DE SERVICIO DE NOTIFICACIONES Y SEGUIMIENTO
 * Define las acciones necesarias para el monitoreo en segundo plano y avisos al usuario.
 */
interface NotificationService {
    
    /**
     * Esto Inicia el rastreo de ubicación en segundo plano.
     */
    fun startBackgroundMonitoring()

    /**
     * Mostrar una notificación de forma rapida, cambien el texto si es que quieren poner otra cosa
     * @param title Título del aviso (ej: "¡PELIGRO!")
     * @param message Cuerpo del mensaje (ej: "Estás entrando en una zona reportada")
     */
    fun sendAlertNotification(title: String, message: String)
}

/**
 * NOTA: usa 'expect' para definir el componente que cada plataforma (Android/iOS)
 * esto se debe implementar de forma nativa debido a las restricciones de los sistemas operativos.
 * pero quien sabe si vaya a funcionar en IOS
 */
expect fun getNotificationService(): NotificationService
