package com.imams.data

import com.imams.core.base.TheResult
import com.imams.data.thenews.implementation.TheNewsRepositoryImpl
import com.imams.data.thenews.repository.TheNewsRepository
import com.imams.data.thenews.source.remote.model.BaseResponse
import com.imams.data.thenews.source.remote.model.ErrorResponse
import com.imams.data.thenews.source.remote.model.HeadlineBundleResponse
import com.imams.data.thenews.source.remote.model.MetaResponse
import com.imams.data.thenews.source.remote.model.NewsResponse
import com.imams.data.thenews.source.remote.service.TheNewsApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test


class NewsRepositoryTest {

    private val apiService: TheNewsApiService = mockk(relaxed = true)
    private lateinit var sut: TheNewsRepository

    @Test
    fun initial_the_news_api_service() {
        val uuid = "s0m3_uu1d"

        coEvery { apiService.getNewsById(uuid, "") }.returns(fakeResponseNewsById(uuid))
        val result = runBlocking {  apiService.getNewsById(uuid, "") }

        // making sure that api service is running on the suspend function
        coVerify { apiService.getNewsById(uuid, any()) }

        assert(result == fakeResponseNewsById(uuid))
    }

    @Test
    fun result_of_top_news() {
        sut = TheNewsRepositoryImpl(apiService)

        val page = 1

        // success response case
        coEvery { apiService.getTopNews(any(), any()) }.returns(
            BaseResponse(data = listOf(), error = null, meta = MetaResponse(found = 0, page = page))
        )
        when (val result = runBlocking { sut.getTopNews(false, page) }) {
            is TheResult.Success -> {
                val data = result.data
                assert(data.meta.page == page)
                assert(data.data.isEmpty())
            }
            else -> {}
        }

        // error response case exception
        coEvery { apiService.getTopNews(any(), any()) }.returns(BaseResponse(error = null, data = null, meta = null))
        when (val result = runBlocking { sut.getTopNews(false, page) }) {
            is TheResult.Error -> {
                assert(result.throwable.message == "data null")
            }
            else -> {}
        }

        // error response case Error Response
        coEvery { apiService.getTopNews(any(), any()) }.returns(
            BaseResponse(error = ErrorResponse("code", "error_message"), data = null, meta = null)
        )
        when (val result = runBlocking { sut.getTopNews(false, page) }) {
            is TheResult.Error -> {
                assert(result.throwable.message == "error_message")
            }
            else -> {}
        }
    }

    @Test
    fun result_of_headline_news() {
        sut = TheNewsRepositoryImpl(apiService)
        coEvery { apiService.getHeadlineNews(any()) }.returns(fakeHeadlineResponse(success = true))

        when (val res = runBlocking { sut.getHeadlineNews(forceLocal = false)}) {
            is TheResult.Success -> {
                val data = res.data
                assert(data.general == null)
                assert(data.health.isNullOrEmpty())
            }
            else -> {}
        }

        // error use case
        coEvery { apiService.getHeadlineNews(any()) }.returns(fakeHeadlineResponse(false))
        when (val res = runBlocking { sut.getHeadlineNews(forceLocal = false)}) {
            is TheResult.Error -> {
                assert(res.throwable.message == "some_message")
            }
            else -> {}
        }
    }

    @Test
    fun result_of_all_news() {

        sut = TheNewsRepositoryImpl(apiService)

        val page = 1

        // success response case
        coEvery { apiService.getAllNews(any(), any()) }.returns(
            BaseResponse(data = listOf(), error = null, meta = MetaResponse(found = 0, page = page))
        )
        when (val result = runBlocking { sut.getAllNews(false, page) }) {
            is TheResult.Success -> {
                val data = result.data
                assert(data.meta.page == page)
                assert(data.data.isEmpty())
            }
            else -> {}
        }

        // error response case Error Response
        coEvery { apiService.getAllNews(any(), any()) }.returns(
            BaseResponse(error = ErrorResponse("code", "error_message"), data = null, meta = null)
        )
        when (val result = runBlocking { sut.getAllNews(false, page) }) {
            is TheResult.Error -> {
                assert(result.throwable.message == "error_message")
            }
            else -> {}
        }
    }

    @Test
    fun result_of_news_by_uuid() {
        sut = TheNewsRepositoryImpl(apiService)

        val slot = slot<String>()
        val uuid = "s0m3_uu1d"
        slot.captured = uuid

        // success response test
        coEvery { apiService.getNewsById(any(), any()) }.returns(fakeResponseNewsById(slot.captured))
        when (val result = runBlocking { sut.getNewsById(uuid) }) {
            is TheResult.Success -> {
                assert(result.data.uuid == slot.captured)
            }
            is TheResult.Error -> {
                assert(result.throwable.message == "data null")
            }
        }

        // error response test
        coEvery { apiService.getNewsById(any(), any()) }.returns(fakeResponseNewsById(""))

        when (val result = runBlocking { sut.getNewsById(uuid) }) {
            is TheResult.Success -> {
                assert(result.data.uuid == slot.captured)
            }
            is TheResult.Error -> {
                assert(result.throwable.message == "data null")
            }
        }
    }

    // region support: fake mocking data
    companion object {

        /**
         * return a response success or failed as fake ApiService result
         */
        fun fakeResponseNewsById(uuid: String? = null) = if (uuid.isNullOrEmpty()) null // error response
        else NewsResponse(uuid = uuid) // success response

        /**
         * return fake response of getHeadlineNews on TheNewsApiService
         */
        fun fakeHeadlineResponse(success: Boolean): BaseResponse<HeadlineBundleResponse> = if (success) {
            val data = HeadlineBundleResponse(null, null, null, null, listOf(), listOf())
            val base = BaseResponse(data = data, error = null, meta = null)
            base
        } else {
            BaseResponse(error = ErrorResponse("some_error", "some_message"), data = null, meta = null)
        }

    }
    // endregion

}