package com.example.android.sunbase_task.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.android.sunbase_task.utils.UrlConverter
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoUrls(val regular: String?,
                     val full: String?,
                     val thumb: String?): Parcelable
