package com.restify.android.ui.screens.home

import com.restify.android.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.restify.android.ui.theme.RobotoCondensedFamily

@Composable
fun InfoScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- Top Header with Back Button ---
        Spacer(modifier = Modifier.height(37.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // --- Back Button ---
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(30.dp)
                )
            }

            // --- Logo ---
            Text(
                text = "RESTIFY",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = RobotoCondensedFamily,
                    fontWeight = FontWeight(700),
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = 5.sp,
                ),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        Spacer(modifier = Modifier.height(30.dp))

        // --- About Us ---
        InfoExpandableCard(title = "About Us") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // Versions
                AboutSectionHeader(icon = "", title = "Versions")
                Text(
                    text = stringResource(R.string.version_content),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
                )

                // About Restify
                AboutSectionHeader(icon = "🚀", title = "About Restify")
                AboutBodyText(stringResource(R.string.about_app_content))

                // Meet the Team
                AboutSectionHeader(icon = "👥", title = "Meet the Team")
                AboutBodyText(stringResource(R.string.about_team_content))
                AboutBodyText(stringResource(R.string.about_team_detailed_content))

                // How We Built It
                AboutSectionHeader(icon = "🛠️", title = "How We Built It")
                AboutBodyText(stringResource(R.string.how_we_built_content))

                // Disclaimer
                AboutSectionHeader(icon = "⚠️", title = "Important Disclaimer")
                AboutBodyText(stringResource(R.string.important_disclaimer))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Terms of Service (Simple Layout) ---
        InfoExpandableCard(title = "Terms of Service") {
            Text(
                text = stringResource(R.string.terms_of_service_content),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = RobotoCondensedFamily,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// --- Helper Components for Styling ---

@Composable
fun AboutSectionHeader(icon: String, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (icon.isNotEmpty()) {
            Text(text = icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp, // 16px Bold
                fontFamily = RobotoCondensedFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
fun AboutBodyText(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 14.sp, // 14px Regular
            fontFamily = RobotoCondensedFamily,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun InfoExpandableCard(
    title: String,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Sync animation to 300ms
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "rotation",
        animationSpec = androidx.compose.animation.core.tween(300)
    )

    Column {
        // --- Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .clickable { expanded = !expanded }
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = RobotoCondensedFamily,
                    fontWeight = FontWeight(700),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            )

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3F414E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand",
                    tint = Color.White,
                    modifier = Modifier.rotate(rotationState)
                )
            }
        }

        // --- Content Section ---
        AnimatedVisibility(
            visible = expanded,
            enter = androidx.compose.animation.expandVertically(animationSpec = androidx.compose.animation.core.tween(300)) + androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.shrinkVertically(animationSpec = androidx.compose.animation.core.tween(300)) + androidx.compose.animation.fadeOut()
        ) {
            // Create a wrapper Column for padding inside the expanded area
            Column(modifier = Modifier.padding(15.dp)) {
                content()
            }
        }
    }
}