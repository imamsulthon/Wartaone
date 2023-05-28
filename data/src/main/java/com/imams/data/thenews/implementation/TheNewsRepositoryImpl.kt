package com.imams.data.thenews.implementation

import com.imams.core.base.TheResult
import com.imams.core.base.toError
import com.imams.data.thenews.mapper.NewsMapper.toModel
import com.imams.data.thenews.model.HeadlineBundle
import com.imams.data.thenews.model.NewsItem
import com.imams.data.thenews.model.NewsWrapper
import com.imams.data.thenews.repository.TheNewsRepository
import com.imams.data.thenews.source.local.DummyDataProvider
import com.imams.data.thenews.source.remote.service.TheNewsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TheNewsRepositoryImpl @Inject constructor(
    private val apiService: TheNewsApiService
) : TheNewsRepository {

    private val locale = "id"
    private val language = "en"

    override suspend fun getHeadlineNews(forceLocal: Boolean): TheResult<HeadlineBundle> {
        return withContext(Dispatchers.IO) {
            try {
                val response = if (forceLocal) DummyDataProvider.headlineResponse()
                else apiService.getHeadlineNews(
                    locale = locale,
                    language = language
                )

                when {
                    response.error != null -> {
                        TheResult.Error(Throwable(message = response.error.message))
                    }
                    response.data == null -> {
                        TheResult.Error(Throwable(message = "data null"))
                    }
                    else -> {
                        TheResult.Success(response.data.toModel())
                    }
                }
            } catch (e: Exception) {
                e.toError()
            }
        }
    }

    override suspend fun getTopNews(forceLocal: Boolean, page: Int): TheResult<NewsWrapper> {
        return withContext(Dispatchers.IO) {
            try {
                val response = if (forceLocal) DummyDataProvider.topNewsResponse() else apiService.getTopNews(
                    page = page,
                    locale = locale,
                    language = language
                )
                when {
                    response.error != null -> {
                        TheResult.Error(Throwable(message = response.error.message))
                    }
                    response.data == null -> {
                        TheResult.Error(Throwable(message = "data null"))
                    }
                    else -> {
                        TheResult.Success(response.toModel())
                    }
                }
            } catch (e: Exception) {
                e.toError()
            }
        }
    }

    override suspend fun getAllNews(forceLocal: Boolean, page: Int): TheResult<NewsWrapper> {
        return withContext(Dispatchers.IO) {
            try {
                val response = if (forceLocal) DummyDataProvider.allNewsResponse() else apiService.getAllNews(
                    page = page,
                    locale = locale, language = language
                )
                when {
                    response.error != null -> {
                        TheResult.Error(Throwable(message = response.error.message))
                    }
                    response.data == null -> {
                        TheResult.Error(Throwable(message = "data null"))
                    }
                    else -> {
                        TheResult.Success(response.toModel())
                    }
                }
            } catch (e: Exception) {
                e.toError()
            }
        }
    }

    override suspend fun getNewsById(uuid: String): TheResult<NewsItem> {
        return withContext(Dispatchers.IO) {
            try {
                when (val response = apiService.getNewsById(
                    uuid = uuid,
                    locale = locale,
                    language = language
                )) {
                    null -> {
                        TheResult.Error(Throwable(message = "data null"))
                    }
                    else -> {
                        TheResult.Success(response.toModel())
                    }
                }
            } catch (e: Exception) {
                e.toError()
            }
        }
    }

    override suspend fun getSimilarNews(uuid: String): TheResult<NewsWrapper> {
        return withContext(Dispatchers.IO) {
            try {
                val forceLocal = false
                val response = if (forceLocal) DummyDataProvider.allNewsResponse() else apiService.getSimilar(
                    page = 1,
                    locale = locale, language = language
                )
                when {
                    response.error != null -> {
                        TheResult.Error(Throwable(message = response.error.message))
                    }
                    response.data == null -> {
                        TheResult.Error(Throwable(message = "data null"))
                    }
                    else -> {
                        TheResult.Success(response.toModel())
                    }
                }
            } catch (e: Exception) {
                e.toError()
            }
        }    }

}