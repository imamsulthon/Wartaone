package com.imams.domain.detail

import com.imams.core.IoDispatcher
import com.imams.core.base.TheResult
import com.imams.data.thenews.repository.TheNewsRepository
import com.imams.domain.model.News
import com.imams.domain.model.mapper.ToViewParamMapper.toViewParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DetailNewsUseCaseImpl(
    private val repository: TheNewsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
): DetailNewsUseCase {

    override suspend fun getDetailNews(uuid: String): Flow<News> {
        return flow {
            when (val result = repository.getNewsById(uuid)) {
                is TheResult.Success -> {
                    emit(result.data.toViewParam()) }
                else -> {
                    emit(News(uuid))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun getSimilarNews(uuid: String): Flow<List<News>> {
        return flow {
            when (val  result = repository.getSimilarNews(uuid)) {
                is TheResult.Success -> {
                    emit(result.data.data.map { it.toViewParam() })
                }
                else -> emit(emptyList())
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun saveAsBookmark(uuid: String) {
        TODO("Not yet implemented")
    }
}