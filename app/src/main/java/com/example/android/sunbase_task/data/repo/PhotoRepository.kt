package com.example.android.sunbase_task.data.repo

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.android.sunbase_task.data.localdatasource.db.AppDataBase
import com.example.android.sunbase_task.data.model.PhotoObject
import com.example.android.sunbase_task.data.model.PhotoResponse
import com.example.android.sunbase_task.data.model.Photoss
import com.example.android.sunbase_task.data.paging.PhotoPagingSource
import com.example.android.sunbase_task.data.paging.PhotoRemoteMediator
import com.example.android.sunbase_task.data.remotedatasource.PhotoService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(
    private val photoService: PhotoService,
    private val db: AppDataBase
) {
    private val pagingSourceFactory = { db.photoDao.getPhotos() }

    /**
     * for caching
     */
    @ExperimentalPagingApi
    fun getPhotos(search: String): Flow<PagingData<Photoss>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = PhotoRemoteMediator(
                photoService,
                db,
                search
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getPhotoLiveData(search: String
    ): LiveData<PagingData<Photoss>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                PhotoPagingSource(photoService, search)
            }
        ).liveData

    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 1
    }
}