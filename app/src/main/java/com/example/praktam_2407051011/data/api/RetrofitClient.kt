package com.example.praktam_2407051011.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://gist.githubusercontent.com/Rosida-p/7ef0c858cd72abe5f54a5d2487abcd3d/raw/c80b743fb918cf187f89716740e4bcef82d50541/"

    val instance: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    }
}