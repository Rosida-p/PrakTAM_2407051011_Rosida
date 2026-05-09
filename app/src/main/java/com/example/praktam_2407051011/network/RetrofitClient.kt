package com.example.praktam_2407051011.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://gist.githubusercontent.com/Rosida-p/7ef0c858cd72abe5f54a5d2487abcd3d/raw/3bf38c93cddb2cdc7488ca7fa3476b1136d5e625/"

    val instance: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    }
}