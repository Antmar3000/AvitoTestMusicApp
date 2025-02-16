package com.am.local.data.repository

import com.am.local.data.local.LocalDataSource
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {
    suspend fun getAudioData () = localDataSource.getAudioData()
}