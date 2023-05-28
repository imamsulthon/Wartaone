package com.imams.domain.model.mapper

import com.imams.data.thenews.model.HeadlineBundle
import com.imams.data.thenews.model.NewsItem
import com.imams.domain.model.HeadlineNews
import com.imams.domain.model.News

object ToViewParamMapper {

    fun NewsItem.toViewParam() = News(
        uuid = uuid,
        title = title,
        description = description,
        keywords = keywords,
        snippet = snippet,
        url = url,
        imageUrl = imageUrl,
        language = language,
        publishedAt = publishedAt,
        source = source,
        categories = categories,
        relevanceScore = relevanceScore,
        locale = locale,
    )

    fun HeadlineBundle.toModel() = HeadlineNews(
        general = general?.map { it.toViewParam() },
        business = general?.map { it.toViewParam() },
        sports = general?.map { it.toViewParam() },
        tech = general?.map { it.toViewParam() },
        science = general?.map { it.toViewParam() },
        health = general?.map { it.toViewParam() },
    )
}