package com.example.proyectoguardia.basededatos

import com.russhwolf.settings.Settings


class StorageService {
    // Conector principal con la base de datos de configuraciones locales
    private val settings: Settings = Settings()


    fun saveData(key: String, value: String) {
        settings.putString(key, value)
    }


    fun getData(key: String): String {
        return settings.getString(key, defaultValue = "Sin datos")
    }
}
