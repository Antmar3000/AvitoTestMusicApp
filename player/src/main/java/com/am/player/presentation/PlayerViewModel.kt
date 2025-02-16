package com.am.player.presentation

import android.app.Application
import android.content.Intent
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.am.core.data.SharedDataManager
import com.am.core.entity.Constants
import com.am.core.entity.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val app: Application,
    private val sharedDataManager: SharedDataManager
) : ViewModel() {

//    val exoPlayer : ExoPlayer by lazy {
//        ExoPlayer.Builder(app).build().apply {
//        }
//    }
//
//    val duration = mutableFloatStateOf(0f)
//
//    val trackState = mutableIntStateOf(0)
//
//    fun setPlayer(mediaItems : List<MediaItem>, index : Int) {
//
//        exoPlayer.setMediaItems(mediaItems, index, 0L)
//        exoPlayer.prepare()
//        trackState.intValue = index
//        exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
//
//        exoPlayer.addListener(object: Player.Listener{
//
//            override fun onPlaybackStateChanged(playbackState: Int) {
//                when (playbackState) {
//                    Player.STATE_READY -> {
//                        exoPlayer.play()
//                        duration.floatValue = exoPlayer.duration.toFloat()
//                    }
//                    Player.STATE_ENDED -> {
//                        exoPlayer.stop()
//                    }
//
//                    Player.STATE_BUFFERING -> {
//
//                    }
//
//                    Player.STATE_IDLE -> {
//
//                    }
//            }
//            }
//
//            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
//                super.onMediaItemTransition(mediaItem, reason)
//                if (reason == 1) {
//                    trackState.intValue += 1
//                    duration.floatValue = exoPlayer.duration.toFloat()
//                }
//            }
//        })
//    }
//
//    val sharedListData : StateFlow<List<Track>?>
//        get() = sharedDataManager.dataList.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.Eagerly,
//        initialValue = null
//    )
//
//    val sharedIndex : StateFlow<Int>
//        get() = sharedDataManager.dataIndex.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.Eagerly,
//        initialValue = -1
//    )
//
//
//    init {
//        viewModelScope.launch {
//            sharedDataManager.data.collect()
//            sharedDataManager.dataList.collect()
//            sharedDataManager.dataIndex.collect()
//        }
//    }
//
//
//    fun playPause() {
////        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
//        val intent = Intent().apply {
//            action = Constants.ACTION_PLAY_PAUSE
//        }
//        LocalBroadcastManager.getInstance(app).sendBroadcast(intent)
//    }
//
//    fun createMediaList (trackList : List<Track>) : List<MediaItem> {
//        val mediaSources = trackList.map { track ->
//            MediaItem.fromUri(track.preview)
//        }
//        return mediaSources
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        exoPlayer.release()
//    }
//
//
//    fun play(mediaItems : List<MediaItem>, index: Int) {
//        val intent = Intent (app, com.am.musictest.presentation.ui.service.service.PlayerForegroundService::class.java).apply {
//            putExtra("list", ArrayList(mediaItems))
//            putExtra("index", index)
//        }
//        app.startService(intent)
//
//    }
}