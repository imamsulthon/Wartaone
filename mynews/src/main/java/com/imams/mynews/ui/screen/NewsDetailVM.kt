package com.imams.mynews.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.domain.detail.DetailNewsUseCase
import com.imams.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailVM @Inject constructor(
    private val useCase: DetailNewsUseCase,
): ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    private val _data = MutableStateFlow(News(""))
    val data: StateFlow<News> = _data

    fun refresh(uuid: String) {
        viewModelScope.launch {
            _isRefreshing.update { true }
            fetchData(uuid)
            _isRefreshing.update { false }
        }
    }

    fun fetchData(uuid: String) {
        viewModelScope.launch {
            _isRefreshing.update { true }
            useCase.getDetailNews(uuid).collectLatest {
                _data.value = it
                _isRefreshing.update { false }
            }
        }
    }

}