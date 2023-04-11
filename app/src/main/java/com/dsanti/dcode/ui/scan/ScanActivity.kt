package com.dsanti.dcode.ui.scan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.dsanti.dcode.R
import com.dsanti.dcode.utils.SystemBarTransparent
import com.dsanti.dcode.ui.theme.DCodeTheme

class ScanActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent { 
            DCodeTheme {
                val context = LocalContext.current
                SystemBarTransparent()
                Box(modifier = Modifier.fillMaxSize()){
                    ScanCameraWithPermissions(context = context)
                    IconButton(onClick = {
                        finish()
                    }, modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .statusBarsPadding()) {
                        Icon(imageVector = Icons.Rounded.Cancel, contentDescription = null, tint = Color.White)
                    }
                }
            }
        }
    }
}


sealed class ScanItems(@StringRes var title : Int){
    object ReadCode : ScanItems(R.string.tab_read_code)
    object UploadCode : ScanItems(R.string.tab_upload_code)
}

