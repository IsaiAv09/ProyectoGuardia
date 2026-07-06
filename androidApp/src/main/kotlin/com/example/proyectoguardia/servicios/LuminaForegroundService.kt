package com.example.proyectoguardia.servicios

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.proyectoguardia.MainActivity

/**
 * SERVICIO DE PRIMER PLANO (FOREGROUND SERVICE)
 * Este servicio permite que la aplicación siga ejecutándose en segundo plano
 * incluso si el usuario cierra la interfaz visual.
 */
class LuminaForegroundService : Service() {

    private val CHANNEL_ID = "LuminaForegroundServiceChannel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Guardian Lumina")
            .setContentText("Protegiendo tu camino en segundo plano")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentIntent(pendingIntent)
            .build()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
            } else {
                startForeground(1, notification)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Si falla al iniciar el primer plano (por ejemplo, falta de permisos), detenemos el servicio
            stopSelf()
        }

        // START_STICKY asegura que el sistema intente recrear el servicio si es matado
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Servicio de Monitoreo Lumina",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
