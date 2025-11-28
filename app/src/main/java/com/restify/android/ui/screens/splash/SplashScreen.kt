package com.restify.android.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.restify.android.R
import com.restify.android.ui.theme.Black
import com.restify.android.ui.theme.Cream
import com.restify.android.ui.theme.RobotoCondensedFamily

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    // 1. Handle the 2-second delay
    LaunchedEffect(key1 = true) {
        delay(2000)
        onSplashFinished()
    }

    // 2. The UI Layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentAlignment = Alignment.Center
    ) {
        // Replace with your actual Logo or Background Shapes
        Image(painter = painterResource(id = R.drawable.bg), contentDescription = null, modifier = Modifier.fillMaxSize())

        Text(
            text = "RESTIFY",
            style = TextStyle(
                fontSize = 40.sp,
                lineHeight = 43.24.sp,
                fontFamily = RobotoCondensedFamily,
                fontWeight = FontWeight(700),
                color = Black,

                textAlign = TextAlign.Center,
                letterSpacing = 10.sp,
            )
        )
    }
}