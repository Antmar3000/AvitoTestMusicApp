package com.am.local.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val localDataFetch: LocalDataFetch
) {
    suspend fun getAudioData () = withContext(Dispatchers.IO) {
        localDataFetch.getAudioData()
    }
}