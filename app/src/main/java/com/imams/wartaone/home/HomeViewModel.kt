package com.imams.wartaone.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.imams.core.base.TheResult
import com.imams.domain.home.HomeUseCase
import com.imams.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase,
): ViewModel() {

    private val _headlineNews = MutableLiveData<List<News>>()
    var headlineNews: LiveData<List<News>> = _headlineNews

    private val _topNews = MutableLiveData<TheResult<List<News>>>()
    var topNews: LiveData<TheResult<List<News>>> = _topNews

    private val _allNewsPaging = MutableLiveData<PagingData<News>>()
    val allNewsPaging: LiveData<PagingData<News>> = _allNewsPaging

    fun fetchData() {
        getHeadline()
        getTopNews()
        getAllNews()
    }

    private fun getHeadline() {
        viewModelScope.launch {
            useCase.getHeadline().collectLatest {
                _headlineNews.postValue(it)
            }
        }
    }

    private fun getTopNews() {
        viewModelScope.launch {
            useCase.getTopNews(1).collectLatest {
                _topNews.postValue(it)
            }
        }
    }

    private fun getAllNews() {
        viewModelScope.launch {
            useCase.getAllNews(1).collectLatest {
                val paging = PagingData.from(it)
                _allNewsPaging.postValue(paging)
            }
        }
    }

}