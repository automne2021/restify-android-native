package com.restify.android.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.restify.android.ui.screens.game.GameScreen
import com.restify.android.ui.screens.home.HomeScreen
import com.restify.android.ui.screens.model3d.Model3DScreen
import com.restify.android.ui.screens.news.NewsScreen
import com.restify.android.ui.screens.video.VideoScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController)}
    ) {
        innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavHost(navHostController = navController)
        }
    }
}

val screenOrder = listOf(
    Screen.Home.route,
    Screen.News.route,
    Screen.Video.route,
    Screen.Model3D.route,
    Screen.Game.route
)

fun getRouteIndex(route: String?): Int = screenOrder.indexOf(route).takeIf { it != -1 } ?: 0

@Composable
fun AppNavHost(navHostController: NavHostController){
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route,
        // Define the Enter Transition (New Screen Appearing)
        enterTransition = {
            val initialIndex = getRouteIndex(initialState.destination.route)
            val targetIndex = getRouteIndex(targetState.destination.route)

            // If going to a higher index (Home -> News), slide in from Right
            if (targetIndex > initialIndex) {
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500))
            } else {
                // If going to a lower index (News -> Home), slide in from Left
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(500))
            }
        },
        // Define the Exit Transition (Old Screen Disappearing)
        exitTransition = {
            val initialIndex = getRouteIndex(initialState.destination.route)
            val targetIndex = getRouteIndex(targetState.destination.route)

            // If going to a higher index, slide out to Left
            if (targetIndex > initialIndex) {
                slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(500))
            } else {
                // If going to a lower index, slide out to Right
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500))
            }
        },
        // Define Pop Transitions (Back Button behavior) - mirrors the above
        popEnterTransition = {
            val initialIndex = getRouteIndex(initialState.destination.route)
            val targetIndex = getRouteIndex(targetState.destination.route)
            if (targetIndex > initialIndex) {
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500))
            } else {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(500))
            }
        },
        popExitTransition = {
            val initialIndex = getRouteIndex(initialState.destination.route)
            val targetIndex = getRouteIndex(targetState.destination.route)
            if (targetIndex > initialIndex) {
                slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(500))
            } else {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500))
            }
        }
    ) {
        composable(Screen.Home.route) { HomeScreen(navHostController) }
        composable(Screen.News.route) { NewsScreen() }
        composable(Screen.Video.route) { VideoScreen() }
        composable(Screen.Model3D.route) { Model3DScreen() }
        composable(Screen.Game.route) { GameScreen() }
    }
}

@Composable
fun BottomNavigationBar(navHostController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.News,
        Screen.Video,
        Screen.Model3D,
        Screen.Game
    );

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .shadow(elevation = 17.dp, spotColor = Color(0x1A54575C), ambientColor = Color(0x1A54575C)),
        windowInsets = WindowInsets.navigationBars.add(WindowInsets(bottom = 42.dp, top = 10.dp, left = 24.5.dp, right = 24.5.dp))
    ) {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                modifier = Modifier,
                label = { },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(width = 46.dp, height = 46.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else Color.Transparent
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.title
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = item.title,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant // Gray
                            }
                        )
                    }
                },
                selected = isSelected,
                onClick = {
                    navHostController.navigate(item.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

    }

}