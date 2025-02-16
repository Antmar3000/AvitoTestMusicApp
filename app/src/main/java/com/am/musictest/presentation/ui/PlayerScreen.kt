package com.am.musictest.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.am.core.entity.Track
import com.am.musictest.R
import com.am.musictest.presentation.ui.service.PlayerForegroundService
import kotlinx.coroutines.delay
import java.time.Duration
import java.util.Locale

@Composable
fun PlayerScreen(trackList: List<Track>,
                 navController: NavController,
                 service: PlayerForegroundService) {

    val exoPlayer = service.exoPlayer

    val indexState = service.indexState

    val progress by produceState(initialValue = 0f) {
        while (true) {
            value = exoPlayer.currentPosition.toFloat()
            delay(100)
        }
    }

    fun formatSecondsToMinutesSeconds(seconds: Float): String {
        val duration = Duration.ofSeconds(seconds.toLong())
        val minutes = duration.toMinutes()
        val remainingSeconds = duration.minusMinutes(minutes).seconds
        return String.format(Locale.ENGLISH,"%d:%02d", minutes, remainingSeconds)
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(horizontalArrangement = Arrangement.End) {
                Spacer(modifier = Modifier.weight(7f))

                Image(
                    painter = painterResource(R.drawable.collapse_icon),
                    contentDescription = "collapse",
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack()
                        }
                        .weight(1f)
                )

                Image(
                    painter = painterResource(R.drawable.close_icon),
                    contentDescription = "close",
                    modifier = Modifier
                        .clickable {
                            exoPlayer.stop()
                            navController.popBackStack()
                        }
                        .weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(trackList[indexState.intValue].album.cover)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.placeholder_icon),
                error = painterResource(R.drawable.placeholder_icon),
                contentDescription = "cover",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(10.dp)),
            )

            Text(modifier = Modifier.padding(horizontal = 8.dp),
                text = trackList[indexState.intValue].title,
                fontSize = 24.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = trackList[indexState.intValue].album.title,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Text(modifier = Modifier.padding(horizontal = 8.dp),
                text = trackList[indexState.intValue].artist.name,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Row (verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)) {

                Slider(modifier = Modifier.weight(9f),
                    value = progress,
                    valueRange = 0f..service.duration.floatValue,
                    onValueChange = { newProgress ->
                        exoPlayer.seekTo(newProgress.toLong())
                    },
                    colors = SliderDefaults.colors(
                        inactiveTickColor = Color.White,
                        activeTrackColor = Color.Cyan,
                        thumbColor = Color.Red
                    )
                )
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier.weight(2f),
                    text = formatSecondsToMinutesSeconds(
                        (service.duration.floatValue.minus(progress)).div(1000)
                    )
                )
            }


            Image(
                painter = painterResource(
                    if (exoPlayer.isPlaying) {
                        R.drawable.pause_icon}
                    else {R.drawable.play_icon })
                 ,
                contentDescription = "play",
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        service.playPause()
                    }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(R.drawable.previous_icon),
                    contentDescription = "previous",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                        if (indexState.intValue == 0) {
                            service.indexState.intValue = trackList.size - 1
                        } else { service.indexState.intValue -= 1 }
                        exoPlayer.seekTo(indexState.intValue, 0L)
                            service.showMusicNotification(true)
                    }
                )

                Image(
                    painter = painterResource(R.drawable.next_icon),
                    contentDescription = "next",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                        if (indexState.intValue == (trackList.size - 1) ) {
                            service.indexState.intValue = 0
                        } else { service.indexState.intValue += 1 }
                        exoPlayer.seekTo(indexState.intValue, 0L)
                            service.showMusicNotification(true)
                    }
                )
            }
        }
    }
}

