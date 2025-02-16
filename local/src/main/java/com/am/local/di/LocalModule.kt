package com.am.local.di

import android.app.Application
import com.am.local.data.LocalDataFetchImpl
import com.am.local.data.local.LocalDataFetch
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideLocalDataFetcher (application: Application) : LocalDataFetch {
        return LocalDataFetchImpl(application)
    }
}