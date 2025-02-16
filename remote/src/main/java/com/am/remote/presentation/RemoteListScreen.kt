package com.am.remote.presentation

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.am.core.entity.Constants
import com.am.core.entity.Track
import com.am.core.presentation.ScreenNav
import com.am.core.presentation.TrackItem
import com.am.remote.utils.NetworkResult
import kotlinx.coroutines.delay

@Composable
fun RemoteListScreen(remoteViewModel: RemoteViewModel, navController: NavController) {

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        delay(200)
        if (searchQuery.isNotEmpty()) {
            remoteViewModel.getFilteredTracks(searchQuery)
        } else remoteViewModel.getMusicItems()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TextField(
            value = searchQuery,
            onValueChange = { newQuery ->
                searchQuery = newQuery
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search...") },
            singleLine = true,
        )

        val state = remoteViewModel.allMusicResponse.collectAsState().value

        val filteredState = remoteViewModel.filteredMusicResponse.collectAsState().value

        if (searchQuery.isEmpty()) {
            when (state) {
                is NetworkResult.Success -> {
                    if (!state.data?.tracks?.data.isNullOrEmpty())
                        SuccessScreen(state.data?.tracks?.data!!, remoteViewModel, navController)
                }

                is NetworkResult.Loading -> {
                    LoadingScreen()
                }

                is NetworkResult.Error -> {
                    ErrorScreen(state.message ?: "someError")
                }
            }
        } else {
            when (filteredState) {
                is NetworkResult.Success -> {
                    if (!filteredState.data?.data.isNullOrEmpty())
                        SuccessScreen(filteredState.data?.data!!, remoteViewModel, navController)
                }

                is NetworkResult.Loading -> {
                    LoadingScreen()
                }

                is NetworkResult.Error -> {
                    ErrorScreen(filteredState.message ?: "someError")
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String) {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = message, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.Red)
    }
}

@Composable
fun SuccessScreen(
    tracks: List<Track>,
    remoteViewModel: RemoteViewModel,
    navController: NavController
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn {
            itemsIndexed(tracks) { index, item ->
                TrackItem(item,
                    onClick = {
                        val intent = Intent().apply {
                            action = Constants.ACTION_PLAY
                        }
                        context.sendBroadcast(intent)
                        remoteViewModel.updateListData(tracks)
                        remoteViewModel.updateIndex(index)
                        navController.navigate(ScreenNav.Player.name)
                    })
            }
        }
    }
}