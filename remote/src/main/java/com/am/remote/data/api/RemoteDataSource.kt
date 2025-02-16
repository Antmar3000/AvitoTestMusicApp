package com.am.remote.data.api

import javax.inject.Inject

class RemoteDataSource @Inject constructor
    (private val musicService : MusicService) {

        suspend fun getMusicItems() = musicService.getTracks()

        suspend fun getFilteredTracks (query: String) = musicService.getFilteredTracks(query)


}
