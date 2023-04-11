package com.dsanti.dcode.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState


@Composable
fun PrivacyPolicy(modifier: Modifier) {


    val state = rememberWebViewState("file:///android_asset/privacy_policy.html")

    WebView(
        state, modifier = modifier
    )

}