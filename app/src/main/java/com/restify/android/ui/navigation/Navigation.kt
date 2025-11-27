package com.restify.android.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import com.restify.android.R

// chứa sealed class Screen, m cmt bằng English thì xóa cmt này của t nha

sealed class RootScreen(val route: String) {
    object Splash : RootScreen("splash")
    object Welcome : RootScreen("welcome")
    object Main : RootScreen("main_graph")
}

sealed class Screen(val route: String, val title: String, @DrawableRes val icon: Int) {
    object Home: Screen(route = "home", title = "Home", icon = R.drawable.ic_home)
    object News: Screen(route = "news", title = "News", icon = R.drawable.ic_news)
    object Video: Screen(route = "video", title = "Video", icon = R.drawable.ic_video)
    object Model3D: Screen(route = "model3d", title = "3D", icon = R.drawable.ic_cube)
    object Game : Screen(route = "game", title = "Game", icon = R.drawable.ic_game)
}
