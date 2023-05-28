package com.imams.domain.detail

import com.imams.domain.model.News
import kotlinx.coroutines.flow.Flow

interface DetailNewsUseCase {

    suspend fun getDetailNews(uuid: String): Flow<News>

    suspend fun getSimilarNews(uuid: String): Flow<List<News>>

    suspend fun saveAsBookmark(uuid: String)

}