package com.am.remote.domain

import com.am.remote.data.repository.RemoteRepository
import javax.inject.Inject

class GetSearchedUseCase @Inject constructor(
        private val remoteRepository: RemoteRepository
) {

    suspend fun invoke (query: String) = remoteRepository.getFilteredTracks(query)
}