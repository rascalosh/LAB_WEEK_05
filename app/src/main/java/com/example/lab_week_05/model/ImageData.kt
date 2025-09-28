// In a file like ImageData.kt
package com.example.lab_week_05.model

import com.squareup.moshi.Json

data class ImageData(
    @field:Json(name = "url") val imageUrl: String?,
    @field:Json(name = "breed_info") val breeds: List<CatBreedData>?

)
            