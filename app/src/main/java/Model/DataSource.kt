package com.example.praktam_2407051011.Model

import android.content.Context

object DataSource {

    // Fungsi mencari gambar drawable berdasarkan nama dari API
    fun getResourceId(context: Context, imageName: String): Int {

        return context.resources.getIdentifier(
            imageName,
            "drawable",
            context.packageName
        )

    }
}