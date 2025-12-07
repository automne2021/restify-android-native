package com.restify.android.ui.screens.news

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.res.painterResource
import com.restify.android.R
import com.restify.android.ui.theme.Orange

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun NewsScreen() {
    var isLoading by remember { mutableStateOf(true) }

    var webView by remember { mutableStateOf<WebView?>(null) }

    BackHandler(enabled = webView?.canGoBack() == true) {
        webView?.goBack()
    }

    DisposableEffect(Unit) {
        onDispose {
            webView?.destroy()
            webView = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        setSupportZoom(true)
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(
                            view: WebView?,
                            url: String?,
                            favicon: Bitmap?
                        ) {
                            isLoading = true
                            webView = view
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            isLoading = false
                            webView = view
                        }
                    }

                    loadUrl("https://vnexpress.net")
                    webView = this
                }
            },
            update = { },
            modifier = Modifier.fillMaxSize()
        )

        Image(
            painter = painterResource(id = R.drawable.watermark),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            alpha = 1.0f
        )

        if (isLoading) {
            CircularProgressIndicator(
                color = Orange,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}