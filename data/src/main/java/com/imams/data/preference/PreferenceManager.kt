package com.imams.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

const val MY_PREFERENCE_DATA_STORE = "my_preference"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    @UserPreference
    fun providePreferenceStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(MY_PREFERENCE_DATA_STORE)
            },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        )
    }

    @Provides
    @Singleton
    fun provideMyPreference(@UserPreference dataStore: DataStore<Preferences>) =
        MyPreference(dataStore)

}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class UserPreference