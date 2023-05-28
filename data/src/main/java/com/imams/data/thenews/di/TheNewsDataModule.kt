package com.imams.data.thenews.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.imams.data.BuildConfig
import com.imams.data.thenews.implementation.TheNewsRepositoryImpl
import com.imams.data.thenews.repository.TheNewsRepository
import com.imams.data.thenews.source.remote.service.TheNewsApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TheNewsDataModule {

    private const val BaseURL = "https://api.thenewsapi.com/v1/news/"
    // todo change to Build.Config or Preference DataStore Manager
    private const val apiToken = "aAJU3nAUFTx99iFC4nFlxyBJTqcn2VHK3qz0Ttou"

    @Provides
    @Singleton
    fun retrofitClient(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        chuckInterceptor: ChuckerInterceptor,
    ) : OkHttpClient {
        val builder =  OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .pingInterval(30, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .addInterceptor {chain ->
                val url = chain.request().url.newBuilder()
                    .addQueryParameter("api_token", apiToken).build()
                val request = chain.request().newBuilder().url(url).build()
                return@addInterceptor chain.proceed(request)
            }
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(chuckInterceptor)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideTheNewsApiService(retrofit: Retrofit): TheNewsApiService {
        return retrofit.create(TheNewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTheNewsRepository(apiService: TheNewsApiService): TheNewsRepository = TheNewsRepositoryImpl(apiService)


}