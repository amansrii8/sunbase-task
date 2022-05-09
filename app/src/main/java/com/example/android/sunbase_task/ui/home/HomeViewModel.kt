package com.example.android.sunbase_task.ui.home

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
class HomeViewModel @Inject constructor(
    private val repository: PhotoRepository
) : ViewModel() {
    private var currentResult: kotlinx.coroutines.flow.Flow<PagingData<Photoss>>? = null

    @ExperimentalPagingApi
    fun searchPhotos(search: String): kotlinx.coroutines.flow.Flow<PagingData<Photoss>> {
        val newResult: kotlinx.coroutines.flow.Flow<PagingData<Photoss>> =
            repository.getPhotos(search).cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }
}