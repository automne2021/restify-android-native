package com.restify.android.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.restify.android.R
import com.restify.android.ui.theme.Black
import com.restify.android.ui.theme.Cream
import com.restify.android.ui.theme.Orange
import com.restify.android.ui.theme.Gray
import com.restify.android.ui.theme.RobotoCondensedFamily

@OptIn(ExperimentalMaterial3Api::class) // Needed for ModalBottomSheet
@Composable
fun WelcomeScreen(onGetStartedClick: () -> Unit) {
    var isChecked by remember { mutableStateOf(false) }
    // State to control the visibility of the Terms Panel
    var showTermsSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
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
                color = Black,
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
                color = Black,
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
                color = Gray,
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        // ILLUSTRATION IMAGE & BUTTONS
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // THE IMAGE
            Image(
                painter = painterResource(id = R.drawable.welcome_illustrator),
                contentDescription = "Welcome Illustrator",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit, // Or .FillWidth based on preference
                alignment = Alignment.BottomCenter
            )

            // THE BUTTONS
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(top = 22.dp, bottom = 60.dp, start = 22.dp, end = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if (isChecked) onGetStartedClick()
                        // Optional: Show error if not checked?
                    },
                    // Disable button if not checked
                    enabled = isChecked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Cream,
                        contentColor = Black,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.5f),
                        disabledContentColor = Color.DarkGray
                    ),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(63.dp)
                ) {
                    Text(
                        text = "GET STARTED",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 15.13.sp,
                            fontFamily = RobotoCondensedFamily,
                            fontWeight = FontWeight(500),
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.7.sp,
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.White,
                            checkmarkColor = Orange,
                            uncheckedColor = Color.White
                        )
                    )

                    // --- CHANGED SECTION: CLICKABLE TEXT ---
                    val termsText = "Terms of Service"
                    val fullText = "I agree to the $termsText"

                    val annotatedString = buildAnnotatedString {
                        // 1. Regular Text
                        withStyle(style = SpanStyle(color = Color.White, fontSize = 12.sp)) {
                            append("I agree to the ")
                        }

                        // Clickable/Underlined Text
                        pushStringAnnotation(tag = "TERMS", annotation = "terms_clicked")
                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(termsText)
                        }
                        pop() // Pop the annotation
                    }

                    ClickableText(
                        text = annotatedString,
                        onClick = { offset ->
                            // Check if the user clicked the "TERMS" part
                            annotatedString.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                                .firstOrNull()?.let {
                                    showTermsSheet = true
                                }
                        }
                    )
                }
            }
        }
    }

    // --- The TOS Modal ---
    if (showTermsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTermsSheet = false },
            containerColor = Cream // Matches your theme
        ) {
            // Content of TOS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()) // Allow scrolling for long text
            ) {
                Text(
                    text = "Terms of Service",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.terms_of_service_content),
                    color = Black,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Close Button
                Button(
                    onClick = { showTermsSheet = false },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Close", color = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}