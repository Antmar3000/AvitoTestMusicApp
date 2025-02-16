package com.am.musictest.presentation.navigation
import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.am.core.presentation.BottomNavItem
import com.am.core.presentation.ScreenNav
import com.am.local.presentation.LocalListScreen
import com.am.local.presentation.LocalViewModel
import com.am.musictest.R
import com.am.musictest.presentation.ui.PlayerScreen
import com.am.musictest.presentation.ui.service.PlayerForegroundService
import com.am.remote.presentation.RemoteListScreen
import com.am.remote.presentation.RemoteViewModel

@Composable
fun Navigation( service: PlayerForegroundService) {

    val localViewModel = hiltViewModel<LocalViewModel>()
    val remoteViewModel = hiltViewModel<RemoteViewModel>()


    val serviceTrackList = service.trackList.value


    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation(navController)
        }
    ) {innerPadding ->
        NavHost(navController, startDestination = ScreenNav.Remote.name,
            modifier = Modifier.padding(innerPadding)) {

            composable (route = ScreenNav.Remote.name) {
                RemoteListScreen(remoteViewModel, navController)
            }

            composable (route = ScreenNav.Local.name) {
                LocalListScreen(localViewModel, navController)
            }

            composable (route = ScreenNav.Player.name) {
                PlayerScreen(serviceTrackList, navController, service)
            }
        }
    }
}

@Composable
fun BottomNavigation (navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem(
            title = ScreenNav.Remote.name,
            selectedIcon = ImageVector.vectorResource(R.drawable.remote),
            unselectedIcon = ImageVector.vectorResource(R.drawable.remote_unselected)
        ),
        BottomNavItem(
            title = ScreenNav.Local.name,
            selectedIcon = ImageVector.vectorResource(R.drawable.local),
            unselectedIcon = ImageVector.vectorResource(R.drawable.local_unselected)
        )
    )

    NavigationBar (modifier = Modifier.height(100.dp)) {
        items.forEachIndexed { _, item ->
            NavigationBarItem(
                selected = currentRoute == item.title,
                onClick = {
                    navController.navigate(item.title)
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == item.title) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}