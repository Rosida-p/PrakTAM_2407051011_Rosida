package com.example.praktam_2407051011.data.repository

import com.example.praktam_2407051011.data.api.RetrofitClient
import com.example.praktam_2407051011.data.model.Aktivitas

class AktivitasRepository {

    suspend fun getAktivitas(): List<Aktivitas> {

        return try {

            RetrofitClient.instance.getAktivitas()

        } catch (e: Exception) {

            emptyList()
        }
    }
}