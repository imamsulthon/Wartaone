package com.imams.data.thenews.model

data class HeadlineBundle(
    var general: List<NewsItem>?,
    var business: List<NewsItem>?,
    var sports: List<NewsItem>?,
    var tech: List<NewsItem>?,
    var science: List<NewsItem>?,
    var health: List<NewsItem>?,
)

data class NewsWrapper(
    val meta: Meta,
    val data: List<NewsItem>
)

data class Meta(
    val found: Int = 0,
    val returned: Int = 0,
    val limit: Int = 0,
    val page: Int = 0,
)

