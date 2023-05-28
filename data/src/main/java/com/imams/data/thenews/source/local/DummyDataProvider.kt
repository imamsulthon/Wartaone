package com.imams.data.thenews.source.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.imams.data.thenews.source.remote.model.BaseResponse
import com.imams.data.thenews.source.remote.model.HeadlineBundleResponse
import com.imams.data.thenews.source.remote.model.NewsResponse
import java.io.IOException

object DummyDataProvider {

    fun headlineResponse(): BaseResponse<HeadlineBundleResponse> {
        val type = object : TypeToken<BaseResponse<HeadlineBundleResponse>>() {}.type
        return Gson().fromJson(DummySource.sHeadline, type)
    }

    fun topNewsResponse(): BaseResponse<List<NewsResponse>> {
        val type = object : TypeToken<BaseResponse<List<NewsResponse>>>() {}.type
        return Gson().fromJson(DummySource.dTopNews, type)
    }

    fun allNewsResponse(): BaseResponse<List<NewsResponse>> {
        val type = object : TypeToken<BaseResponse<List<NewsResponse>>>() {}.type
        return Gson().fromJson(DummySource.dAllNews, type)
    }

}


// todo hmm need context???
fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}