package com.restify.android.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.restify.android.ui.screens.game.GameScreen
import com.restify.android.ui.screens.home.HomeScreen
import com.restify.android.ui.screens.model3d.Model3DScreen
import com.restify.android.ui.screens.news.NewsScreen
import com.restify.android.ui.screens.video.VideoScreen

@Composable
fun MainScreen() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    BackHandler(enabled = selectedIndex != 0) {
        selectedIndex = 0
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                onTabSelected = { selectedIndex = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Use KeepAliveScreen for all tabs to prevent recreation
            KeepAliveScreen(visible = selectedIndex == 0) {
                HomeScreen(onNavigationToTab = { index ->
                    selectedIndex = index
                })
            }
            KeepAliveScreen(visible = selectedIndex == 1) {
                NewsScreen()
            }
            KeepAliveScreen(visible = selectedIndex == 2) {
                VideoScreen()
            }
            KeepAliveScreen(visible = selectedIndex == 3) {
                Model3DScreen()
            }
            KeepAliveScreen(visible = selectedIndex == 4) {
                GameScreen()
            }
        }
    }
}

@Composable
fun KeepAliveScreen(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(x = if (visible) 0.dp else 10000.dp)
    ) {
        content()
    }
}

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        Screen.Home,
        Screen.News,
        Screen.Video,
        Screen.Model3D,
        Screen.Game
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .shadow(
                elevation = 17.dp,
                spotColor = Color(0x1A54575C),
                ambientColor = Color(0x1A54575C)
            ),
        windowInsets = WindowInsets.navigationBars.add(
            WindowInsets(
                bottom = 42.dp,
                top = 10.dp,
                left = 24.5.dp,
                right = 24.5.dp
            )
        )
    ) {

        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index

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
                    onTabSelected(index)
                }
            )
        }

    }
}