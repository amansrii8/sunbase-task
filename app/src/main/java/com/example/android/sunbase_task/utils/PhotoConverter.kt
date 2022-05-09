package com.example.android.sunbase_task.utils

import androidx.room.TypeConverter
import com.example.android.sunbase_task.data.model.PhotoObject
import com.example.android.sunbase_task.data.model.PhotoUrls
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class PhotoConverter {

    private val gSon = Gson()

    private val type: Type = object : TypeToken<PhotoUrls?>() {}.type

    @TypeConverter
    fun from(photoObject: PhotoUrls?): String? {
        if (photoObject == null) {
            return null
        }

        return gSon.toJson(photoObject, type)
    }

    @TypeConverter
    fun to(photoString: String?): PhotoUrls? {
        if (photoString == null) {
            return null
        }
        return gSon.fromJson(photoString, type)
    }
}