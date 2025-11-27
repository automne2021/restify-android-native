package com.restify.android.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.restify.android.R
import com.restify.android.ui.theme.RobotoCondensedFamily

@Composable
fun WelcomeScreen(onGetStartedClick: () -> Unit) {
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Logo Text
        Text(
            text = "RESTIFY",
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 32.sp,
                fontFamily = RobotoCondensedFamily,
                fontWeight = FontWeight(700),
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 5.sp,
            )
        )

        Spacer(modifier = Modifier.height(114.dp))

        // Welcome Box
        Text(
            text = "Welcome to Restify!",
            style = TextStyle(
                fontSize = 32.sp,
                lineHeight = 34.59.sp,
                fontFamily = RobotoCondensedFamily,
                fontWeight = FontWeight(700),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = "Explore the app, Find some peace of mind.",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = RobotoCondensedFamily,
                fontWeight = FontWeight(300),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        // ILLUSTRATION IMAGE
        Image(
            painter = painterResource(id = R.drawable.welcome_illustrator),
            contentDescription = "Welcome Illustrator",
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit,
            alignment = Alignment.BottomCenter
        )

        // Bottom Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0A348)) // darker orange/brown bottom area
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onGetStartedClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "GET STARTED",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 15.13.sp,
                        fontFamily = RobotoCondensedFamily,
                        fontWeight = FontWeight(500),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.7.sp,
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color.White, checkmarkColor = Color.Black)
                )
                Text(text = "I agree to the Terms of Service", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}