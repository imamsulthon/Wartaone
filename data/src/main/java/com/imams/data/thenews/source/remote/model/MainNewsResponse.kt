package com.imams.data.thenews.source.remote.model

import com.google.gson.annotations.SerializedName

data class HeadlineBundleResponse (
    @SerializedName("general" ) var general : List<NewsResponse>?,
    @SerializedName("business" ) var business : List<NewsResponse>?,
    @SerializedName("sports" ) var sports : List<NewsResponse>?,
    @SerializedName("tech" ) var tech : List<NewsResponse>?,
    @SerializedName("science" ) var science : List<NewsResponse>?,
    @SerializedName("health" ) var health : List<NewsResponse>?,
)