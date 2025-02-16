package com.am.remote.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.am.core.data.SharedDataManager
import com.am.core.entity.Track
import com.am.remote.data.api.models.Data
import com.am.remote.data.api.models.TrackResponse
import com.am.remote.domain.GetRemoteListUseCase
import com.am.remote.domain.GetSearchedUseCase
import com.am.remote.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteViewModel @Inject constructor (
    private val getRemotePagedUseCase: GetRemoteListUseCase,
    private val getSearchedUseCase: GetSearchedUseCase,
    private val sharedDataManager: SharedDataManager
) : ViewModel() {

    private val _allMusicResponse = MutableStateFlow<NetworkResult<TrackResponse>>(NetworkResult.Loading())
    val allMusicResponse : StateFlow<NetworkResult<TrackResponse>> get() = _allMusicResponse

    private val _filteredMusicResponse = MutableStateFlow<NetworkResult<Data>>(NetworkResult.Loading())
    val filteredMusicResponse : StateFlow<NetworkResult<Data>> get() = _filteredMusicResponse

    init {
        getMusicItems()
    }

    fun getMusicItems () {
        viewModelScope.launch(Dispatchers.IO) {
            getRemotePagedUseCase.invoke().let {
                _allMusicResponse.value = it
            }
        }
    }

    fun getFilteredTracks (query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getSearchedUseCase.invoke(query).let {
                _filteredMusicResponse.value = it
            }
        }
    }

    fun updateListData (list : List<Track>) {
        viewModelScope.launch(Dispatchers.IO) {
            sharedDataManager.updateDataList(list)
        }
    }

    fun updateIndex (index : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            sharedDataManager.updateDataIndex(index)
        }
    }
}
