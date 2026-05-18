package com.example.praktam_2407051011.data.api

import com.example.praktam_2407051011.data.model.Aktivitas
import retrofit2.http.GET

interface ApiService {

    @GET("aktivitas_harian.json")
    suspend fun getAktivitas(): List<Aktivitas>

}