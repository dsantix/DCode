package com.dsanti.dcode.ui.scan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.UploadFile
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.dsanti.dcode.R
import com.dsanti.dcode.data.AppDatabase
import com.dsanti.dcode.data.QRCode
import com.dsanti.dcode.ui.theme.AppTypography
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import kotlinx.coroutines.launch



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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun UploadOverlay(bottomSheetState: ModalBottomSheetState, uploadViewModel: ScanViewModel = viewModel(), db: AppDatabase) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()


    val launcher = rememberLauncherForActivityResult(contract = CropImageContract(),
        onResult = { result ->
            when {
                result.isSuccessful -> {
                    
                    val inputStream = result.uriContent?.let {
                        context.contentResolver.openInputStream(
                            it
                        )
                    }

                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    val pixels = intArrayOf(bitmap.width * bitmap.height)
                    bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

                    val source = RGBLuminanceSource(bitmap.width, bitmap.height, pixels)
                    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

                    val reader = MultiFormatReader()
                    val resultDecode = reader.decode(binaryBitmap)

                    scope.launch {
                        uploadViewModel.resultUpload.value = resultDecode
                        addQRToDatabase(result = resultDecode, db = db)
                        bottomSheetState.show()
                    }
                }
            }
        })


    Box(Modifier.fillMaxSize()) {
        AlphaUploadQrBackground()
        Column(Modifier.align(Alignment.Center)) {
            Text(text = "Fazer upload da imagem",
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                style = AppTypography.displaySmall)

            Card(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                launcher.launch(options {
                    setGuidelines(CropImageView.Guidelines.ON)
                })
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

private fun addQRToDatabase(result: Result, db: AppDatabase){
    val qrDao = db.qrCodeDao()

    qrDao.insertAll(QRCode(qrCodeDate = result.timestamp, qrCodeText = result.text))
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