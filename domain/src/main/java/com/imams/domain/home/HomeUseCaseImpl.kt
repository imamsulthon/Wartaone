package com.imams.domain.home

import com.imams.core.base.TheResult
import com.imams.data.preference.MyPreference
import com.imams.data.thenews.model.HeadlineBundle
import com.imams.data.thenews.repository.TheNewsRepository
import com.imams.domain.model.News
import com.imams.domain.model.mapper.ToViewParamMapper.toViewParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeUseCaseImpl @Inject constructor(
    private val repository: TheNewsRepository,
    private val preference: MyPreference,
) : HomeUseCase {

    override suspend fun getPreferences(): Flow<Boolean> {
        return preference.isForceLocal()
    }

    override suspend fun savePreferences(set: Boolean) {
        preference.forceLocal(set)
    }

    override suspend fun getHeadline(): Flow<List<News>> {
        return flow {
            when (val result = repository.getHeadlineNews(forceLocal = true)) {
                is TheResult.Success -> {
                    val data = result.data
                    emit(data.justTakeEachOne())
                }
                is TheResult.Error -> {
                    emit(emptyList())
                }
            }
        }
    }

    /**
     * This should return only 1 page for Home use case
     */
    override suspend fun getTopNews(page: Int): Flow<TheResult<List<News>>> {
        return flow {
            val forceLocal = preference.isForceLocal().first()
            when (val result = repository.getTopNews(forceLocal,page)) {
                is TheResult.Success -> {
                    val data = result.data.data.map { it.toViewParam() }
                    emit(TheResult.Success(data))
                }
                is TheResult.Error -> {
                    emit(TheResult.Error(result.throwable))
                }
            }
        }
    }

    /**
     * This should return only 1 page for Home use case
     */
    override suspend fun getAllNews(page: Int): Flow<List<News>> {
        return flow {
            val forceLocal = preference.isForceLocal().first()
            when (val result = repository.getAllNews(forceLocal, page)) {
                is TheResult.Success -> {
                    val data = result.data.data
                    emit(data.map { it.toViewParam() })
                }
                is TheResult.Error -> {
                    emit(emptyList())
                }
            }
        }
    }

    private fun HeadlineBundle.justTakeEachOne(): List<News> {
        val list = mutableListOf<News>()
        this.let {
            general?.first()?.let { n -> list.add(n.toViewParam()) }
            business?.first()?.let { n -> list.add(n.toViewParam()) }
            sports?.first()?.let { n -> list.add(n.toViewParam()) }
            science?.first()?.let { n -> list.add(n.toViewParam()) }
            health?.first()?.let { n -> list.add(n.toViewParam()) }
        }
        return list
    }

}