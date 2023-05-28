package com.imams.domain.home

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.imams.core.base.TheResult
import com.imams.data.preference.MyPreference
import com.imams.data.thenews.repository.TheNewsRepository
import com.imams.domain.model.News
import com.imams.domain.model.mapper.ToViewParamMapper.toViewParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface AllNewsUseCase {

    suspend fun getPaginationFlow(tag: String): Flow<PagingData<News>>

}

class AllNewsUseCaseImpl @Inject constructor(
    private val repository: TheNewsRepository,
    private val preference: MyPreference,
): AllNewsUseCase {

    override suspend fun getPaginationFlow(tag: String): Flow<PagingData<News>> {
        val forceLocal = preference.isForceLocal().first()
        return Pager(
            config = PagingConfig(pageSize = 3, enablePlaceholders = false),
            pagingSourceFactory = {
                if (tag.equals("top", true)) {
                    TopNewsPagingSource(repository, forceLocal)
                } else AllNewsPagingSource(repository, forceLocal)
            }
        ).flow
    }

}

internal class TopNewsPagingSource(
    private val repository: TheNewsRepository,
    private val forceLocal: Boolean = false
): PagingSource<Int, News>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        val page = params.key ?: 1
        return try {
            when (val response = repository.getTopNews(forceLocal, page)) {
                is TheResult.Success -> {
                    val list = response.data.data.map { it.toViewParam() }
                    LoadResult.Page(
                        list,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (list.isEmpty()) null else page + 1
                    )
                }
                is TheResult.Error -> {
                    LoadResult.Error(response.throwable)
                }
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}

internal class AllNewsPagingSource(
    private val repository: TheNewsRepository,
    private val forceLocal: Boolean = false
): PagingSource<Int, News>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        val page = params.key ?: 1
        return try {
            when (val response = repository.getAllNews(forceLocal, page)) {
                is TheResult.Success -> {
                    val meta = response.data.meta
                    val list = response.data.data.map { it.toViewParam() }
                    LoadResult.Page(
                        list,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (list.isEmpty()) null else page + 1
                    )
                }
                is TheResult.Error -> {
                    LoadResult.Error(response.throwable)
                }
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}