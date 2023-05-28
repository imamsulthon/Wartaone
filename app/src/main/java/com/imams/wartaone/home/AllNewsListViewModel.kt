package com.imams.wartaone.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.imams.domain.home.AllNewsUseCase
import com.imams.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AllNewsListViewModel @Inject constructor(
    private val useCase: AllNewsUseCase,
): ViewModel() {

    suspend fun fetchData(tag: String): Flow<PagingData<News>> = useCase.getPaginationFlow(tag)
        .cachedIn(viewModelScope)

}