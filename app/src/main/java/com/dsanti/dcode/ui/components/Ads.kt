package com.dsanti.dcode.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdBannerView(isTest:Boolean = true, modifier: Modifier = Modifier) {
    val adUnitId = if (isTest) "ca-app-pub-3940256099942544/6300978111" else "ca-app-pub-5453458161087883/5409138492"

    AndroidView(modifier = modifier,factory = {
        AdView(it).apply {
            setAdSize(AdSize.FULL_BANNER)
            setAdUnitId(adUnitId)
            loadAd(AdRequest.Builder().build())
        }
    })
}