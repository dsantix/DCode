package com.dsanti.dcode.ui.settings

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun PrivacyPolicy(modifier: Modifier) {


    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                loadUrl("file:///android_asset/privacy_policy.html")
            }
        },
        modifier = modifier
    )

}