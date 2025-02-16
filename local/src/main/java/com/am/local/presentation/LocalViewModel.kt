package com.am.local.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.am.core.entity.Track
import com.am.local.domain.GetLocalListUseCase
import com.am.core.data.SharedDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor (
    private val getLocalListUseCase: GetLocalListUseCase,
    private val sharedDataManager: SharedDataManager
) : ViewModel() {

    val musicList = mutableStateOf(listOf<Track>())

    init {
        getInitialAudioData()
    }

    private fun getInitialAudioData () {
        viewModelScope.launch {
            musicList.value = getLocalListUseCase.invoke()
        }
    }

    fun updateListData (list : List<Track>) {
        viewModelScope.launch {
            sharedDataManager.updateDataList(list)
        }
    }

    fun updateIndex (index : Int) {
        viewModelScope.launch {
            sharedDataManager.updateDataIndex(index)
        }
    }
}