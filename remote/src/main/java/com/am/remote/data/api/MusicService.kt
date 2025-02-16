package com.am.remote.data.api

import com.am.remote.data.api.models.Data
import com.am.remote.data.api.models.TrackResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicService {

    @GET("chart")
    suspend fun getTracks(): Response<TrackResponse>

    @GET("search")
    suspend fun getFilteredTracks (
        @Query("q") query: String
    ) : Response<Data>

}