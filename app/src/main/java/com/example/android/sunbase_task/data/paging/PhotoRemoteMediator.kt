package com.example.android.sunbase_task.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.android.sunbase_task.data.localdatasource.db.AppDataBase
import com.example.android.sunbase_task.data.localdatasource.db.entity.RemoteKeys
import com.example.android.sunbase_task.data.model.Photoss
import com.example.android.sunbase_task.data.remotedatasource.PhotoService
import com.example.android.sunbase_task.utils.STARTING_PAGE_INDEX
import com.example.android.sunbase_task.utils.client_id
import retrofit2.HttpException
import java.io.IOException


@ExperimentalPagingApi
class PhotoRemoteMediator(
    private val service: PhotoService,
    private val db: AppDataBase,
    private val search: String
) : RemoteMediator<Int, Photoss>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Photoss>
    ): MediatorResult {
        val key = when (loadType) {
            LoadType.REFRESH -> {
                if (db.photoDao.count() > 0) return MediatorResult.Success(false)
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                getKey()
            }
        }

        try {
            if (key != null) {
                if (key.isEndReached) return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page: Int = key?.nextKey ?: STARTING_PAGE_INDEX
            val apiResponse = service.getPhotos(
                page,
                search, client_id
            )

            val photoList = apiResponse.results

            val endOfPaginationReached =
                page == apiResponse.total_pages

            db.withTransaction {
                val nextKey = page + 1

                db.remoteKeysDao.insertKey(
                    RemoteKeys(
                        0,
                        nextKey = nextKey,
                        isEndReached = endOfPaginationReached
                    )
                )
                db.photoDao.insertPhotoList(photoList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKey(): RemoteKeys? {
        return db.remoteKeysDao.getKeys().firstOrNull()
    }


}