package com.am.musictest.presentation.ui.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.am.core.data.SharedDataManager
import com.am.core.entity.Constants
import com.am.core.entity.Constants.CHANNEL_ID
import com.am.core.entity.Constants.CHANNEL_NAME
import com.am.core.entity.Constants.NOTIFICATION_ID
import com.am.core.entity.Track
import com.am.musictest.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlayerForegroundService : Service() {

    @Inject
    lateinit var sharedDataManager: SharedDataManager

    private lateinit var notificationManager: NotificationManager

    val exoPlayer : ExoPlayer by lazy {
        ExoPlayer.Builder(this).build().apply {
            playWhenReady = true
        }
    }

    inner class LocalBinder : Binder() {
        fun getService() : PlayerForegroundService = this@PlayerForegroundService
    }
    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    val trackList = mutableStateOf<List<Track>>(emptyList())

    val indexState = mutableIntStateOf(0)

    val duration = mutableFloatStateOf(0f)

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter().apply {
            addAction(Constants.ACTION_PLAY)
            addAction(Constants.ACTION_PAUSE)
            addAction(Constants.ACTION_RESUME)
            addAction(Constants.ACTION_PREVIOUS)
            addAction(Constants.ACTION_NEXT)
        }
        registerReceiver(receiver, filter, RECEIVER_EXPORTED)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        scope.launch {
            sharedDataManager.dataList.collect {value ->
                trackList.value = value
            }
        }
        scope.launch {
            sharedDataManager.dataIndex.collect {value ->
                indexState.intValue = value
            }
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
        }

    private fun createNotification(notificationLayout : RemoteViews) : Notification {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setCustomContentView(notificationLayout)
            .setSmallIcon(R.drawable.placeholder_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setDefaults(0)
            .setOnlyAlertOnce(true)
            .build()
        return notification
    }

    private fun createNotificationChannel() {
            val name = CHANNEL_NAME
            val descriptionText = Constants.ACTION_NEXT
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                setSound(null , null)
                enableVibration(false)
            }
        notificationManager.createNotificationChannel(channel)

    }

    fun createMediaList (trackList : List<Track>) : List<MediaItem> {
        val mediaSources = trackList.map { track ->
            MediaItem.fromUri(track.preview)
        }
        return mediaSources
    }

    fun playPause() {
        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Constants.ACTION_PLAY -> {
                    setPlayer(createMediaList(trackList.value), indexState.intValue)
                    if (ActivityCompat.checkSelfPermission(
                            this@PlayerForegroundService,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        showMusicNotification(true)
                    }

                }

                Constants.ACTION_PAUSE -> {
                    exoPlayer.pause()
                    showMusicNotification(false)
                }

                Constants.ACTION_RESUME -> {
                    exoPlayer.play()
                    showMusicNotification(true)
                }

                Constants.ACTION_NEXT -> {
                    if (indexState.intValue == (trackList.value.size - 1) ) {
                        indexState.intValue = 0
                    } else { indexState.intValue += 1 }
                    exoPlayer.seekTo(indexState.intValue, 0L)
                    showMusicNotification(true)
                }

                Constants.ACTION_PREVIOUS -> {
                    if (indexState.intValue == 0) {
                        indexState.intValue = trackList.value.size - 1
                    } else { indexState.intValue -= 1 }
                    exoPlayer.seekTo(indexState.intValue, 0L)
                    showMusicNotification(true)
                }
            }
        }
    }

    fun setPlayer(mediaItems : List<MediaItem>, index : Int) {

        exoPlayer.setMediaItems(mediaItems, index, 0L)
        exoPlayer.prepare()
        indexState.intValue = index
        exoPlayer.repeatMode = Player.REPEAT_MODE_OFF

        exoPlayer.addListener(object: Player.Listener{

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        exoPlayer.play()
                        duration.floatValue = exoPlayer.duration.toFloat()
                    }
                    Player.STATE_ENDED -> {
                        exoPlayer.stop()
                    }

                    Player.STATE_BUFFERING -> {

                    }

                    Player.STATE_IDLE -> {

                    }
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                if (reason == 1) {
                    indexState.intValue += 1
                    duration.floatValue = exoPlayer.duration.toFloat()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    fun showMusicNotification (playState : Boolean) {

        val notificationView = RemoteViews(this.packageName, R.layout.notification_layout)

        notificationView.setTextViewText(R.id.text_name, trackList.value[indexState.intValue].title)
        notificationView.setImageViewResource(R.id.playpause, if (playState) R.drawable.pause_icon else R.drawable.play_icon)

        val playPauseIntent = Intent(if (playState) Constants.ACTION_PAUSE else Constants.ACTION_RESUME)
        val nextIntent = Intent(Constants.ACTION_NEXT)
        val previousIntent = Intent(Constants.ACTION_PREVIOUS)

        val playPausePendingIntent = PendingIntent.getBroadcast(
            this, 1, playPauseIntent,
            PendingIntent.FLAG_IMMUTABLE)

        val nextPendingIntent = PendingIntent.getBroadcast(this, 1, nextIntent, PendingIntent.FLAG_IMMUTABLE)
        val prevPendingIntent = PendingIntent.getBroadcast(this, 1, previousIntent, PendingIntent.FLAG_IMMUTABLE)

        notificationView.setOnClickPendingIntent(R.id.playpause, playPausePendingIntent)
        notificationView.setOnClickPendingIntent(R.id.previous, prevPendingIntent)
        notificationView.setOnClickPendingIntent(R.id.next, nextPendingIntent)

        startForeground(NOTIFICATION_ID, createNotification(notificationView))
        notificationManager.notify(NOTIFICATION_ID, createNotification(notificationView))
    }
}