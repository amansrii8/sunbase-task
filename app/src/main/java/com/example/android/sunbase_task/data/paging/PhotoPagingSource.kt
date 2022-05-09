package com.example.android.sunbase_task.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.sunbase_task.data.model.PhotoObject
import com.example.android.sunbase_task.data.model.PhotoResponse
import com.example.android.sunbase_task.data.model.Photoss
import com.example.android.sunbase_task.data.remotedatasource.PhotoService
import com.example.android.sunbase_task.utils.STARTING_PAGE_INDEX
import com.example.android.sunbase_task.utils.client_id
import retrofit2.HttpException
import java.io.IOException

class PhotoPagingSource(private val photoService: PhotoService, private val search: String) :
    PagingSource<Int, Photoss>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photoss> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = photoService.getPhotos(params.loadSize, search, client_id)
            val images = response
            LoadResult.Page(
                data = images.results.toList(),
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.total_pages) null else page + 1
            )

        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Photoss>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}