package com.imams.data.thenews.source.remote.service

import com.imams.data.thenews.source.remote.model.BaseResponse
import com.imams.data.thenews.source.remote.model.HeadlineBundleResponse
import com.imams.data.thenews.source.remote.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheNewsApiService {

    @GET("headlines")
    suspend fun getHeadlineNews(
        @Query("locale") locale: String? = "us",
        @Query("language") language: String? = "en",
    ): BaseResponse<HeadlineBundleResponse>

    @GET("top")
    suspend fun getTopNews(
        @Query("page") page: Int,
        @Query("locale") locale: String? = "us",
        @Query("language") language: String? = "en",
        @Query("limit") limit: Int? = 10
    ): BaseResponse<List<NewsResponse>>

    @GET("all")
    suspend fun getAllNews(
        @Query("page") page: Int,
        @Query("locale") locale: String? = "us",
        @Query("language") language: String? = "en",
        @Query("limit") limit: Int? = 10,
    ): BaseResponse<List<NewsResponse>>

    @GET("uuid/{uuid}")
    suspend fun getNewsById(
        @Path("uuid") uuid: String,
        @Query("locale") locale: String? = "us",
        @Query("language") language: String? = "en",
    ): NewsResponse?

    @GET("similar")
    suspend fun getSimilar(
        @Query("page") page: Int,
        @Query("locale") locale: String? = "us",
        @Query("language") language: String? = "en",
        @Query("limit") limit: Int? = 10,
    ): BaseResponse<List<NewsResponse>>

}