package com.imams.mynews.ui.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.core.base.TheResult
import com.imams.domain.home.HomeUseCase
import com.imams.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenVM @Inject constructor(
    private val useCase: HomeUseCase,
): ViewModel() {

    private val _onRefresh = MutableStateFlow(false)
    val onRefresh: StateFlow<Boolean> = _onRefresh

    private val _headlineNews = mutableStateListOf<News>()
    private val _headlineNewsFlow = MutableStateFlow<List<News>>(listOf())
    val headlineNews: StateFlow<List<News>> = _headlineNewsFlow

    private val _topNews = MutableStateFlow<List<News>>(listOf())
    val topNews: StateFlow<List<News>> = _topNews

    private val _allNews = MutableStateFlow<List<News>>(listOf())
    val allNews: StateFlow<List<News>> = _allNews

    val loading1 = MutableStateFlow(true)
    val loading2 = MutableStateFlow(true)
    val loading3 = MutableStateFlow(true)

    fun fetchData() {
        getHeadlineNews()
        getTopNews()
        getAllNews()
    }

    fun refresh() {
        viewModelScope.launch {
            _onRefresh.update { true }
            fetchData()
            delay(500)
            _onRefresh.update { false }
        }
    }

    private fun getHeadlineNews() {
        viewModelScope.launch {
            loading1.update { true }
            useCase.getHeadline().collectLatest { list ->
                log("collect Head list ${list.size}")
                _headlineNews.addAll(list)
                loading1.update { false }
            }
        }
    }

    private fun getTopNews() {
        viewModelScope.launch {
            loading2.update { true }
            useCase.getTopNews(1).collectLatest { res ->
                log("collect TOP list $res")
                when (res) {
                    is TheResult.Success -> {
                        _topNews.value = res.data
                        loading2.update { false }
                    }
                    is TheResult.Error -> {
                        loading2.update { false }
                    }
                }
            }
        }
    }

    private fun getAllNews() {
        viewModelScope.launch {
            loading3.update { true }
            useCase.getAllNews(1).collectLatest {
                log("collect ALL list ${it.size}")
                val list = it + it
                _allNews.value = list
                loading3.update { false }
            }
        }
    }

    private fun log(msg: String) = println("HomeVM: $msg")

}