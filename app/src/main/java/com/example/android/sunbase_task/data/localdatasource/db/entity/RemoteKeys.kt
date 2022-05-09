package com.example.android.sunbase_task.data.localdatasource.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val nextKey: Int?,
    val isEndReached: Boolean
)