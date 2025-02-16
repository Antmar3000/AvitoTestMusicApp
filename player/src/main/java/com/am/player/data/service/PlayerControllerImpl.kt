package com.am.player.data.service

import android.app.Application
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.am.player.domain.PlayerController
import javax.inject.Inject


class PlayerControllerImpl @Inject constructor(
    private val application: Application
) : PlayerController {

    private var exoPlayer : ExoPlayer? = null



    override fun playTrack (path : String) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(application).build()
        }
        exoPlayer?.setMediaItem(MediaItem.fromUri(path))
        exoPlayer?.prepare()
        exoPlayer?.play()
    }

    override fun pauseTrack () {
        exoPlayer?.pause()
    }

    fun release () {
        exoPlayer?.release()
        exoPlayer = null
    }

}