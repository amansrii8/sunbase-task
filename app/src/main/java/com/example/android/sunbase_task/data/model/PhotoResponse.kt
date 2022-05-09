package com.example.android.sunbase_task.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoResponse(val total: Int, val total_pages: Int,
                         val results: ArrayList<Photoss>) :Parcelable

