package com.example.android.sunbase_task.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.android.sunbase_task.utils.PhotoConverter
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
@TypeConverters(PhotoConverter::class)
@Entity(tableName = "photo_tables")
data class Photoss(
    @PrimaryKey(autoGenerate = true)
    val pid: Int,
    val id: String?,
    val urls: PhotoUrls?,
    ) : Parcelable
