package com.example.praktam_2407051011.Model

import com.google.gson.annotations.SerializedName

data class Aktivitas(

    @SerializedName("judul")
    val judul: String,

    @SerializedName("jam")
    val jam: String,

    @SerializedName("deskripsi")
    val deskripsi: String,

    @SerializedName("image_url")
    val imageUrl: String

)