package com.am.remote.data.repository

import com.am.remote.data.api.RemoteDataSource
import com.am.remote.data.api.SafeApiResponse
import com.am.remote.data.api.models.Data
import com.am.remote.data.api.models.TrackResponse
import com.am.remote.utils.NetworkResult
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : SafeApiResponse() {

    suspend fun getSafeTracks() : NetworkResult<TrackResponse> {
        return safeApiCall { remoteDataSource.getMusicItems() }
    }

    suspend fun getFilteredTracks (query: String) : NetworkResult<Data> {
        return safeApiCall { remoteDataSource.getFilteredTracks(query) }
    }



}