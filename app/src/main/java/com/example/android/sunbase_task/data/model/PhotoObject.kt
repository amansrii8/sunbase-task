package com.example.android.sunbase_task.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoObject(val id: String?, val urls: PhotoUrls?) : Parcelable
