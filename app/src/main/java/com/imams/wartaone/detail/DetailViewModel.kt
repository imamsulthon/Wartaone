package com.imams.wartaone.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.domain.detail.DetailNewsUseCase
import com.imams.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val useCase: DetailNewsUseCase
): ViewModel() {

    private val _data = MutableLiveData<News>()
    val data: LiveData<News> = _data

    private val _similarNews = MutableLiveData<List<News>>()
    val similarNews: LiveData<List<News>> = _similarNews

    fun fetchData(uuid: String) {
        viewModelScope.launch {
            useCase.getDetailNews(uuid).collectLatest {
                println("DetailVm $uuid $it")
                _data.postValue(it)
            }
        }
    }

    fun getSimilar(uuid: String) {
        viewModelScope.launch {
            useCase.getSimilarNews(uuid).collectLatest {
                _similarNews.postValue(it)
            }
        }
    }

}