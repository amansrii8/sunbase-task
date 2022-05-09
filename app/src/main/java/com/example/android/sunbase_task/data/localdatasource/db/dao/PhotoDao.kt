package dev.ronnie.allplayers.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.sunbase_task.data.model.PhotoObject
import com.example.android.sunbase_task.data.model.Photoss

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotoList(list: List<Photoss>)

    @Query("SELECT * FROM photo_tables")
    fun getPhotos(): PagingSource<Int, Photoss>

    @Query("DELETE FROM photo_tables")
    suspend fun clearRepos()

    @Query("SELECT COUNT(id) from photo_tables")
    suspend fun count(): Int

}