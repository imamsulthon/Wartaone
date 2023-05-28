package com.imams.data.thenews.mapper

import com.imams.data.thenews.model.HeadlineBundle
import com.imams.data.thenews.model.Meta
import com.imams.data.thenews.model.NewsItem
import com.imams.data.thenews.model.NewsWrapper
import com.imams.data.thenews.source.remote.model.BaseResponse
import com.imams.data.thenews.source.remote.model.HeadlineBundleResponse
import com.imams.data.thenews.source.remote.model.MetaResponse
import com.imams.data.thenews.source.remote.model.NewsResponse

object NewsMapper {

    fun NewsResponse.toModel() = NewsItem(
        uuid = uuid.orEmpty(),
        title = title.orEmpty(),
        description = description.orEmpty(),
        keywords = keywords.orEmpty(),
        snippet = snippet.orEmpty(),
        url = url.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        language = language.orEmpty(),
        publishedAt = publishedAt.orEmpty(),
        source = source.orEmpty(),
        categories = categories,
        relevanceScore = relevanceScore.orEmpty(),
        locale = locale.orEmpty(),
    )

    fun MetaResponse.toModel() = Meta(
        found = found ?: 0,
        returned = returned ?: 0,
        limit = limit ?: 0,
        page = page ?: 0,
    )

    fun HeadlineBundleResponse.toModel() = HeadlineBundle(
        general = general?.map { it.toModel() },
        business = general?.map { it.toModel() },
        sports = general?.map { it.toModel() },
        tech = general?.map { it.toModel() },
        science = general?.map { it.toModel() },
        health = general?.map { it.toModel() },
    )

    fun BaseResponse<List<NewsResponse>>.toModel() = NewsWrapper(
        meta = meta?.toModel() ?: Meta(),
        data = data?.map { it.toModel() } ?: emptyList(),
    )
}