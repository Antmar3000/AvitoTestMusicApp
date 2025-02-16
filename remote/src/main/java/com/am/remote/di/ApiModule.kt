package com.am.remote.di

import android.net.Uri
import com.am.remote.data.api.MusicService
import com.am.remote.data.api.models.UriDeserializer
import com.am.remote.data.api.models.UriSerializer
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val BASE_ULR = "https://api.deezer.com/"

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor () =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        }

    @Singleton
    @Provides
    fun provideOkHttp (interceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    private val gson = GsonBuilder()
        .registerTypeAdapter(Uri::class.java, UriDeserializer())
        .registerTypeAdapter(Uri::class.java, UriSerializer())
        .create()

    @Singleton
    @Provides
    fun provideRetrofit (okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(BASE_ULR)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideMusicService (retrofit: Retrofit) = retrofit.create(MusicService::class.java)
}