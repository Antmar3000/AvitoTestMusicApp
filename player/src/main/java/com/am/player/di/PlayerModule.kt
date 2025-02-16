package com.am.player.di

import android.app.Application
import com.am.player.data.service.PlayerControllerImpl
import com.am.player.domain.PlayerController
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Provides
    @Singleton
    fun bindPlayerController (app : Application) : PlayerController {
        return PlayerControllerImpl(app)
    }
}