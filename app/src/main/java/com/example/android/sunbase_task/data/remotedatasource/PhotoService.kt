package com.example.android.sunbase_task.data.remotedatasource

import com.example.android.sunbase_task.data.model.PhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoService {

    @GET("search/photos")
    suspend fun getPhotos(@Query("page") page: Int, @Query("query") query: String,
    @Query("client_id") id: String) : PhotoResponse
}