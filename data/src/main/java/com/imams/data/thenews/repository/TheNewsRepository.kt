package com.imams.data.thenews.repository

import com.imams.core.base.TheResult
import com.imams.data.thenews.model.HeadlineBundle
import com.imams.data.thenews.model.NewsItem
import com.imams.data.thenews.model.NewsWrapper

interface TheNewsRepository {

    suspend fun getHeadlineNews(forceLocal: Boolean): TheResult<HeadlineBundle>
    suspend fun getTopNews(forceLocal: Boolean, page: Int): TheResult<NewsWrapper>
    suspend fun getAllNews(forceLocal: Boolean, page: Int): TheResult<NewsWrapper>
    suspend fun getNewsById(uuid: String): TheResult<NewsItem>
    suspend fun getSimilarNews(uuid: String): TheResult<NewsWrapper>

}