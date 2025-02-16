package com.am.core.presentation

import androidx.compose.ui.graphics.vector.ImageVector

enum class ScreenNav  {
    Remote,
    Local,
    Player
}

data class BottomNavItem (
    val title : String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector
)
