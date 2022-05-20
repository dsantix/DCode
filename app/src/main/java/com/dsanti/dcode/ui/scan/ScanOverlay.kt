package com.dsanti.dcode.ui.scan

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dsanti.dcode.R
import com.dsanti.dcode.ui.theme.AppTypography


@Composable
fun ScanOverlay() {
    Box(Modifier.fillMaxSize()) {
        //AlphaReadQrBackground()
        Text(text = stringResource(id = R.string.tab_read_code),
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 120.dp),
            style = AppTypography.displaySmall)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadOverlay() {
    Box(Modifier.fillMaxSize()) {
        AlphaUploadQrBackground()
        val context = LocalContext.current
        Column(Modifier.align(Alignment.Center)) {
            Text(text = "Fazer upload da imagem",
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                style = AppTypography.displaySmall)

            Card(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                Toast.makeText(context, "Upload Image", Toast.LENGTH_SHORT).show()
            }) {
                Row(Modifier.padding(16.dp)){
                    Icon(imageVector = Icons.Rounded.UploadFile, contentDescription = null)
                    Text(text = "Upar imagem do QR Code", modifier = Modifier
                        .align(CenterVertically)
                        .padding(end = 8.dp))
                }
            }
        }
    }
}

@Composable
fun AlphaReadQrBackground() {
    Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
        val rectPath = Path().apply {
            addRoundRect(RoundRect(rect = Rect(center, size.minDimension / 3), cornerRadius = CornerRadius(8f)))
        }
        clipPath(rectPath, clipOp = ClipOp.Difference) {
            drawRect(SolidColor(Color.Black.copy(alpha = 0.8f)))
        }
    })
}

@Composable
fun AlphaUploadQrBackground() {
    Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
        Path().apply {
            drawRect(SolidColor(Color.Black.copy(alpha = 0.8f)))        }
    })
}

@Preview
@Composable
fun PreviewScanActivity() {
    ScanOverlay()
}

@Preview
@Composable
fun PreviewUploadActivity() {
    UploadOverlay()
}