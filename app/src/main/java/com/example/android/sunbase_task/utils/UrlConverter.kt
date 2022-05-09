package com.example.android.sunbase_task.utils
import androidx.room.TypeConverter
import com.example.android.sunbase_task.data.model.PhotoObject
import com.example.android.sunbase_task.data.model.PhotoUrls
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class UrlConverter {

    private val gSon = Gson()

    private val type: Type = object : TypeToken<PhotoObject?>() {}.type

    @TypeConverter
    fun from(photoUrls: PhotoUrls?): String? {
        if (photoUrls == null) {
            return null
        }

        return gSon.toJson(photoUrls, type)
    }



    @TypeConverter
    fun to(urls: String?): PhotoUrls? {
        if (urls == null) {
            return null
        }
        return gSon.fromJson(urls, type)
    }
}