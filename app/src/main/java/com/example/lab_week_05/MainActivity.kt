package com.example.lab_week_05

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import com.example.lab_week_05.api.CatApiService
import com.example.lab_week_05.model.ImageData
import retrofit2.converter.moshi.MoshiConverterFactory
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val catApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }
    private val apiResponseView: TextView by lazy {
        findViewById(R.id.api_response)
    }
    private val imageResultView: ImageView by lazy {
        findViewById(R.id.image_result)
    }
    private val imageLoader: ImageLoader by lazy {
        GlideLoader(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getCatImageResponse()
    }

    private fun getCatImageResponse() {
        val call = catApiService.searchImages(limit = 1, format = "full", has_breeds = 1)
        call.enqueue(object : Callback<List<ImageData>> {
            override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
                Log.e(MAIN_ACTIVITY, "Failed to get image response: ${t.message}", t)
                apiResponseView.text = "Error loading cat info."
            }

            override fun onResponse(
                call: Call<List<ImageData>>,
                response: Response<List<ImageData>>
            ) {
                if (response.isSuccessful) {
                    val imageList = response.body()
                    Log.d(MAIN_ACTIVITY, "Full API Response: $imageList")

                    val firstImageData = imageList?.firstOrNull()

                    // Extract URL
                    val imageUrl = firstImageData?.imageUrl
                    Log.d(MAIN_ACTIVITY, "Extracted Image URL: $imageUrl")

                    if (!imageUrl.isNullOrBlank()) {
                        imageLoader.loadImage(imageUrl, imageResultView)
                    } else {
                        Log.d(MAIN_ACTIVITY, "No image URL available")
                        apiResponseView.text = "No image found."
                    }


                    val breedName = firstImageData?.breeds?.firstOrNull()?.name ?: "Unknown"
                    apiResponseView.text = getString(R.string.breed_display_simple, breedName)
                    Log.d(MAIN_ACTIVITY, "Displayed Breed: $breedName")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(MAIN_ACTIVITY, "Failed response. Code: ${response.code()}, Error: $errorBody")
                    apiResponseView.text = "Failed to load cat details."
                }
            }
        })
    }



    companion object {
        const val MAIN_ACTIVITY = "MainActivityLog"
    }
}
