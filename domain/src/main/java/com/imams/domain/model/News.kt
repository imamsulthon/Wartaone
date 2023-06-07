package com.imams.domain.model

data class HeadlineNews(
    var general: List<News>?,
    var business: List<News>?,
    var sports: List<News>?,
    var tech: List<News>?,
    var science: List<News>?,
    var health: List<News>?,
)

data class News(
    val uuid: String,
    val title: String,
    val description: String,
    val keywords: String,
    val snippet: String,
    val url: String,
    val imageUrl: String,
    val language: String,
    val publishedAt: String,
    val source: String,
    val categories: List<String> = listOf(),
    val relevanceScore: String,
    val locale: String,
) {
    constructor(uuid: String): this(uuid, "", "", "", "", "", "",
        "", "", "", listOf(),"","")
    constructor(uuid: String, title: String, desc: String): this("", title, desc, "", "", "", "",
        "", "", "", listOf(),"","")

}
