package com.example.proyectoguardia

import com.russhwolf.settings.Settings

class StorageService {
    private val settings: Settings = Settings()

    fun saveData(key: String, value: String) {
        settings.putString(key, value)
    }

    fun getData(key: String): String {
        return settings.getString(key, defaultValue = "Sin datos")
    }
}