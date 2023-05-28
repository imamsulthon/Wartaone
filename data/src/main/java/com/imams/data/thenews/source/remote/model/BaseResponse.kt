package com.imams.data.thenews.source.remote.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("meta")
    val meta: MetaResponse?,
    @SerializedName("data")
    val data: T?,
    @SerializedName("error")
    val error: ErrorResponse?,
)

data class ErrorResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
)