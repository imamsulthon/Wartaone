package com.imams.domain.home

import com.imams.core.base.TheResult
import com.imams.domain.model.News
import kotlinx.coroutines.flow.Flow

interface HomeUseCase {

    suspend fun getPreferences(): Flow<Boolean>
    suspend fun savePreferences(set: Boolean)
    suspend fun getHeadline(): Flow<List<News>>
    suspend fun getTopNews(page: Int): Flow<TheResult<List<News>>>
    suspend fun getAllNews(page: Int): Flow<List<News>>

}