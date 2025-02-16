package com.am.musictest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import com.am.musictest.presentation.navigation.Navigation
import com.am.musictest.presentation.ui.Permissions
import com.am.core.presentation.theme.TestMusicAppTheme
import com.am.musictest.presentation.ui.service.PlayerForegroundService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var service : PlayerForegroundService? = null

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val serviceBinder = binder as PlayerForegroundService.LocalBinder
            service = serviceBinder.getService()
            serviceState.value = service!!
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            service = null
        }
    }

    val serviceState = mutableStateOf(PlayerForegroundService())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, PlayerForegroundService::class.java)
        startForegroundService(intent)

        this.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        enableEdgeToEdge()
        setContent {
            TestMusicAppTheme {

                Permissions()
                Navigation(serviceState.value)

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        this.unbindService(serviceConnection)
    }
}



