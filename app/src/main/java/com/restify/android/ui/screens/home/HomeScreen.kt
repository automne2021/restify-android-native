package com.restify.android.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.restify.android.R
import com.restify.android.ui.navigation.Screen
import com.restify.android.ui.theme.Black
import com.restify.android.ui.theme.Cream
import com.restify.android.ui.theme.Gray
import com.restify.android.ui.theme.RobotoCondensedFamily
import com.restify.android.ui.theme.White

fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }

        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    var showInfoDialog by remember { mutableStateOf(false) }

    if (showInfoDialog) {
        InfoDialog (onDismiss = { showInfoDialog = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState)
    ) {
        // --- Top Header ---
        Spacer(modifier = Modifier.height(37.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // --- Logo ---
            Text(
                text = "RESTIFY",
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 32.sp,
                    fontFamily = RobotoCondensedFamily,
                    fontWeight = FontWeight(700),
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = 5.sp,
                ),
                textAlign = TextAlign.Center
            )
            // --- Info icon ---
            IconButton(
                onClick = { showInfoDialog = true },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "Info",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(Modifier.height(51.dp))

        // --- Greeting Section ---
        Text(
            text = stringResource(R.string.home_greeting),
            style = TextStyle(
                fontSize = 28.sp,
                lineHeight = 30.27.sp,
                fontFamily = RobotoCondensedFamily,
                fontWeight = FontWeight(700),
                color = MaterialTheme.colorScheme.onBackground,
            )
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.home_sub_greeting),
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 21.62.sp,
                fontFamily = RobotoCondensedFamily,
                fontWeight = FontWeight(400),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )

        Spacer(Modifier.height(45.dp))

        // --- Grid Cards ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // News Card
            HomeGridCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.home_card_breaking_news),
                subtitle = "READ NOW",
                imageRes = R.drawable.news,
                onClick = { navigateToTab(navController, Screen.News.route) }
            )
            // Video Card
            HomeGridCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.home_card_watch_videos),
                subtitle = "WATCH NOW",
                imageRes = R.drawable.video,
                onClick = { navigateToTab(navController, Screen.Video.route) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 3D Card ---
        WideActionCard(
            title = stringResource(R.string.home_card_3d_models),
            subtitle = "PLAY NOW",
            onClick = { navigateToTab(navController, Screen.Model3D.route) }
        )

        Spacer(modifier = Modifier.height(34.dp))

        Text(
            text = "Game on!",
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 25.94.sp,
                fontFamily = RobotoCondensedFamily,
                fontWeight = FontWeight(500),
                color = MaterialTheme.colorScheme.onBackground,

                )
        )

        Spacer(modifier = Modifier.height(14.dp))

        WideActionCard(
            title = stringResource(R.string.home_card_games),
            subtitle = "PLAY NOW",
            onClick = { navigateToTab(navController, Screen.Game.route) }
        )

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun HomeGridCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    imageRes: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(210.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black.copy(alpha = 0.25f),
                ambientColor = Color.Black.copy(alpha = 0.25f)
            )
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Column(Modifier.fillMaxSize()) {
            // Illustration Image Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.wrapContentSize(),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                modifier = Modifier.padding(start = 15.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = RobotoCondensedFamily,
                    fontWeight = FontWeight(700),
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            )

            Spacer(modifier = Modifier.height(45.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = subtitle,
                    modifier = Modifier.padding(start = 15.dp),
                    style = TextStyle(
                        fontSize = 11.sp,
                        lineHeight = 11.89.sp,
                        fontFamily = RobotoCondensedFamily,
                        fontWeight = FontWeight(700),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.55.sp,
                    )
                )

                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    shape = RoundedCornerShape(50),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 17.dp, vertical = 10.dp),
                    modifier = Modifier.padding(end = 11.dp)
                ) {
                    Text(
                        text = "START",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 12.97.sp,
                            fontFamily = RobotoCondensedFamily,
                            fontWeight = FontWeight(700),
                            color = MaterialTheme.colorScheme.onTertiary,
                            letterSpacing = 0.6.sp,
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun WideActionCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(10.dp),
                spotColor = Color.Black.copy(alpha = 0.25f),
                ambientColor = Color.Black.copy(alpha = 0.25f)
            )
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                color = Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        // Play button
        Button(
            onClick = onClick,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Cream
            ),
            modifier = Modifier.size(40.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = "Play",
                tint = Black,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}