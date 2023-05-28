package com.imams.domain

import com.imams.core.IoDispatcher
import com.imams.data.preference.MyPreference
import com.imams.data.thenews.repository.TheNewsRepository
import com.imams.domain.detail.DetailNewsUseCase
import com.imams.domain.detail.DetailNewsUseCaseImpl
import com.imams.domain.home.AllNewsUseCase
import com.imams.domain.home.AllNewsUseCaseImpl
import com.imams.domain.home.HomeUseCase
import com.imams.domain.home.HomeUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    @ViewModelScoped
    fun provideHomeUseCase(
        repository: TheNewsRepository,
        myPreference: MyPreference,
    ): HomeUseCase {
        return HomeUseCaseImpl(repository, myPreference)
    }

    @Provides
    @ViewModelScoped
    fun provideDetailUseCase(
        repository: TheNewsRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): DetailNewsUseCase = DetailNewsUseCaseImpl(repository, dispatcher)

    @Provides
    @ViewModelScoped
    fun provideAllNewsPagingUseCase(
        repository: TheNewsRepository,
        myPreference: MyPreference,
    ): AllNewsUseCase = AllNewsUseCaseImpl(repository, myPreference)

}