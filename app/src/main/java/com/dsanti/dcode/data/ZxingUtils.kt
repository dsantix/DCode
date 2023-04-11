package com.dsanti.dcode.data

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer


@androidx.camera.core.ExperimentalGetImage
class ZxingQrCodeAnalyzer(
    private val onQrCodesDetected: (qrCode: Result) -> Unit
) : ImageAnalysis.Analyzer {

    companion object {
        val reader = MultiFormatReader()
    }

    /*
        https://developer.android.com/training/camerax/configuration

        Default resolution: The default target resolution setting is 640x480.

        Adjusting both target resolution and corresponding aspect ratio will result
        in a best-supported resolution under 1080p (max analysis resolution).
    */
    override fun analyze(imageProxy: ImageProxy) {
        // okay - manage rotation, not needed for QRCode decoding [-;
        // okay - manage it for barcode scanning instead!!!
        try {
            imageProxy.image?.let {
                // ImageProxy uses an ImageReader under the hood:
                // https://developer.android.com/reference/androidx/camera/core/ImageProxy.html
                // That has a default format of YUV_420_888 if not changed.
                // https://developer.android.com/reference/android/graphics/ImageFormat.html#YUV_420_888
                // https://developer.android.com/reference/android/media/ImageReader.html
                if ((it.format == ImageFormat.YUV_420_888
                            || it.format == ImageFormat.YUV_422_888
                            || it.format == ImageFormat.YUV_444_888)
                    && it.planes.size == 3) {
                    val buffer = it.planes[0].buffer // We get the luminance plane only, since we
                    // want to binarize it and we don't wanna take color into consideration.
                    val bytes = ByteArray(buffer.capacity())
                    buffer.get(bytes)
                    // Create a LuminanceSource.
                    val rotatedImage = RotatedImage(bytes, imageProxy.width, imageProxy.height)

                    val source = PlanarYUVLuminanceSource(rotatedImage.byteArray,
                        rotatedImage.width,
                        rotatedImage.height,
                        0,
                        0,
                        rotatedImage.width,
                        rotatedImage.height,
                        false)

                    // Create a Binarizer
                    val binarizer = HybridBinarizer(source)
                    // Create a BinaryBitmap.
                    val binaryBitmap = BinaryBitmap(binarizer)
                    // Try decoding...
                    val result: Result
                    try {
                        result = reader.decodeWithState(binaryBitmap)
                        onQrCodesDetected(result)
                    } catch (e: NotFoundException) {
                        println(e.message)
                    }
                }
            }
        } catch (ise: IllegalStateException) {
            println(ise.printStackTrace())
        }


    }
}

private data class RotatedImage(var byteArray: ByteArray, var width: Int, var height: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RotatedImage

        if (!byteArray.contentEquals(other.byteArray)) return false
        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }

    override fun hashCode(): Int {
        var result = byteArray.contentHashCode()
        result = 31 * result + width
        result = 31 * result + height
        return result
    }
}