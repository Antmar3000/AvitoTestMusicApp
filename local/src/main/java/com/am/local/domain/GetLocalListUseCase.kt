package com.am.local.domain

import com.am.local.data.repository.LocalRepository
import javax.inject.Inject

class GetLocalListUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend fun invoke() = localRepository.getAudioData()
}