package com.example.proyectoguardia.servicios

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat

/**
 * BACK END para android
 * Esta clase usa las herramientas propias de Android para enviar notificaciones
 * y manejar procesos que no se mueren al cerrar la pantalla.
 */
class AndroidNotificationService(private val context: Context) : NotificationService {

    private val channelId = "danger_alerts"
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        //  Crea el canal de comunicación con el sistema Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Alertas de Seguridad",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones para zonas de peligro"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Rastreo de ubicación en segundo plano.
     * No tocar. con esto se activaría un 'Foreground Service' con GPS constante.
     */
    override fun startBackgroundMonitoring() {
        println("SISTEMA: Activando receptor de ubicación en segundo plano...")
    }

    /**
     *  Dispara la notificación visual y sonora en el teléfono.
     * Se activa cuando la app detecta que estás cerca de una zona marcada como peligrosa.
     */
    override fun sendAlertNotification(title: String, message: String) {
        // Vibración del dispositivo
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(500)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}

// Variable global temporal para el contexto (Se inicializa en MainActivity)
lateinit var androidContext: Context

actual fun getNotificationService(): NotificationService = AndroidNotificationService(androidContext)
