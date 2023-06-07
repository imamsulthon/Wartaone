package com.imams.mynews.ui.screen

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
class SeeAllNewsVM @Inject constructor(
    private val useCase: AllNewsUseCase,
): ViewModel() {

    val data : Flow<PagingData<News>> = useCase.getPaginationFlow("all", false)
        .cachedIn(viewModelScope)

    fun fetchData(): Flow<PagingData<News>> = useCase.getPaginationFlow("all",false)
        .cachedIn(viewModelScope)

}