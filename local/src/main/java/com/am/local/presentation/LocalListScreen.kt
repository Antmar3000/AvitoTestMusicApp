package com.am.local.presentation

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.am.core.entity.Constants
import com.am.core.presentation.ScreenNav
import com.am.core.presentation.TrackItem
import kotlinx.coroutines.delay

@Composable
fun LocalListScreen(localViewModel: LocalViewModel, navController: NavController) {

    val listItems = localViewModel.musicList.value

    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var delayedQuery by remember { mutableStateOf("") }

    val filteredList = listItems.filter { track ->
        track.title.contains(delayedQuery, ignoreCase = true) ||
                track.artist.name.contains(delayedQuery, ignoreCase = true)
    }

    LaunchedEffect(searchQuery) {
        delay(200)
        delayedQuery = searchQuery
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
            singleLine = true
        )

        if (filteredList.isEmpty()) {
            Text("Nothing found")
        } else {
            LazyColumn {
                itemsIndexed(filteredList) { index, item ->
                    TrackItem(
                        item,
                        onClick = {

                            val intent = Intent().apply {
                                action = Constants.ACTION_PLAY
                            }
                            context.sendBroadcast(intent)
                            localViewModel.updateListData(filteredList)
                            localViewModel.updateIndex(index)
                            navController.navigate(ScreenNav.Player.name)
                        },
                    )
                }
            }
        }
    }
}
