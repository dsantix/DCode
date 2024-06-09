package com.dsanti.dcode.ui

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
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

//Copied from accompanist guide


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


