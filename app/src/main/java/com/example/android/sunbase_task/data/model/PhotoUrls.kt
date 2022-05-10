package com.example.android.sunbase_task.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoUrls(val regular: String?,
                     val full: String?,
                     val thumb: String?): Parcelable
