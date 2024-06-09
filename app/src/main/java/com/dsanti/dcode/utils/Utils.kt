package com.dsanti.dcode.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.MediaColumns.IS_PENDING
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream




@Composable
fun ComposableLifecycle(
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}


/*
    Copied from accompanist library and adapted to work with Material3
 */


@RequiresApi(Build.VERSION_CODES.Q)
fun saveImageInQ(bitmap: Bitmap, context: Context): Uri? {
    val filename = "qrcode_${System.currentTimeMillis()}.jpg"
    var fos: OutputStream?
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        put(MediaStore.Video.Media.IS_PENDING, 1)
    }

    //use application context to get contentResolver
    val contentResolver = context.applicationContext.contentResolver
    val uri = contentResolver.insert(EXTERNAL_CONTENT_URI, contentValues)
    uri?.let { contentResolver.openOutputStream(it) }.also { fos = it }
    fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
    fos?.flush()
    fos?.close()

    contentValues.clear()
    contentValues.put(IS_PENDING, 0)
    uri?.let {
        contentResolver.update(it, contentValues, null, null)
    }

    return uri
}

fun legacySave(bitmap: Bitmap, context: Context): Uri {
    val appContext = context.applicationContext
    val filename = "qrcode_${System.currentTimeMillis()}.jpg"
    val directory = getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
    val file = File(directory, filename)
    val outStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
    outStream.flush()
    outStream.close()
    MediaScannerConnection.scanFile(appContext, arrayOf(file.absolutePath),
        null, null)
    return FileProvider.getUriForFile(appContext, "${appContext.packageName}.provider",
        file)
}


fun ImageBitmap.Companion.generateQRCode(data:String, @ColorInt color: Int = Color.White.toArgb(), @ColorInt backgroundColor : Int = Color.Black.toArgb()) : ImageBitmap{
    val hints = hashMapOf<EncodeHintType, Any>().also {
        it[EncodeHintType.CHARACTER_SET] = "utf-8"
        it[EncodeHintType.MARGIN] = 1
        it[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
    }

    val size = 512

    val code = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size, hints)

    return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
        for (x in 0 until size) {
            for (y in 0 until size) {
                it.setPixel(
                    x, y,
                    if (code[x,y]) color else backgroundColor
                )
            }
        }
    }.asImageBitmap()
}

