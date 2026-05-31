package com.example.proyectoguardia

class GuardiaService {
    private val apiService = ApiService()
    private val storageService = StorageService()

    suspend fun getAndSaveData(): String {
        val data = apiService.fetchData()
        storageService.saveData("last_api_response", data)
        return data
    }

    fun getLocalData(): String {
        return storageService.getData("last_api_response")
    }
}
