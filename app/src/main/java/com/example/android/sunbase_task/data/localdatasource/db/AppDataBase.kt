package com.example.android.sunbase_task.data.localdatasource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.sunbase_task.data.localdatasource.db.dao.RemoteKeysDao
import com.example.android.sunbase_task.data.localdatasource.db.entity.RemoteKeys
import com.example.android.sunbase_task.data.model.PhotoObject
import com.example.android.sunbase_task.data.model.Photoss
import dev.ronnie.allplayers.data.dao.PhotoDao


@Database(
    entities = [Photoss::class, RemoteKeys::class],
    version = 2, exportSchema = false
)

abstract class AppDataBase : RoomDatabase() {
    abstract val photoDao: PhotoDao
    abstract val remoteKeysDao: RemoteKeysDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance ?: buildDatabase(
                    context
                ).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "app_db"
            ).fallbackToDestructiveMigration()
                .build()
    }
}