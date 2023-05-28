package com.imams.data.thenews.model

data class NewsItem(
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
)
