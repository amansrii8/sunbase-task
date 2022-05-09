package com.example.android.sunbase_task.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.android.sunbase_task.data.model.Photoss
import com.example.android.sunbase_task.data.repo.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: PhotoRepository) : ViewModel() {

    private var currentResult: kotlinx.coroutines.flow.Flow<PagingData<Photoss>>? = null

    @ExperimentalPagingApi
    fun searchPhotos(search: String): kotlinx.coroutines.flow.Flow<PagingData<Photoss>> {
        val newResult: kotlinx.coroutines.flow.Flow<PagingData<Photoss>> =
            repository.getPhotos(search)
        currentResult = newResult
        return newResult
    }

    /**
     * Same thing but with Livedata
     */
    private var currentResultLiveData: LiveData<PagingData<Photoss>>? = null

    fun searchPhotoLiveData(search: String): LiveData<PagingData<Photoss>> {
        val newResultLiveData: LiveData<PagingData<Photoss>> =
            repository.getPhotoLiveData(search).cachedIn(viewModelScope)
        currentResultLiveData = newResultLiveData
        return newResultLiveData
    }

}
