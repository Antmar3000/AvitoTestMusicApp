package com.am.core.data

import com.am.core.entity.Track
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedDataManager @Inject constructor() {

    private val _dataList = MutableSharedFlow<List<Track>>(replay = 1)
    val dataList : SharedFlow<List<Track>> get() = _dataList

    private val _dataIndex = MutableSharedFlow<Int>(replay = 1)
    val dataIndex : SharedFlow<Int> get() = _dataIndex


    suspend fun updateDataList (list : List<Track>) {
        _dataList.emit(list)
    }

    suspend fun updateDataIndex (index : Int) {
        _dataIndex.emit(index)
    }

}