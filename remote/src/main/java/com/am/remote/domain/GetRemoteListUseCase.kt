package com.am.remote.domain

import com.am.remote.data.repository.RemoteRepository
import javax.inject.Inject

class GetRemoteListUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
){

    suspend fun invoke () = remoteRepository.getSafeTracks()

}