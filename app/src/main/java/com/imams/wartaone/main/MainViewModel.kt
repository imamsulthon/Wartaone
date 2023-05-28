package com.imams.wartaone.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.domain.home.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: HomeUseCase,
): ViewModel() {

    private val _preference = MutableLiveData<Boolean>()
    val switch: LiveData<Boolean> = _preference

    fun fetchData() {
        viewModelScope.launch {
            useCase.getPreferences().collectLatest { useLocal ->
                _preference.postValue(!useLocal)
            }
        }
    }

    fun useApi(set: Boolean) {
        viewModelScope.launch {
            useCase.savePreferences(!set)
        }
    }

}