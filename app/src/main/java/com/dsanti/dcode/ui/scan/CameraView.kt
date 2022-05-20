package com.dsanti.dcode.ui.scan

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.MutableLiveData
import com.dsanti.dcode.ui.components.TabRounded
import com.dsanti.dcode.ui.scan.components.BottomSheetResult
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import kotlinx.coroutines.launch



@OptIn(ExperimentalPermissionsApi::class, ExperimentalPagerApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun ScanCameraWithPermissions(scanViewModel: ScanViewModel = viewModel()) {

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    val result by scanViewModel.result.observeAsState()


    when (cameraPermissionState.status) {
        // If the camera permission is granted, then show screen with the feature enabled
        PermissionStatus.Granted -> {
            val pagerState = rememberPagerState(0)
            val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
            val scope = rememberCoroutineScope()
            val tabs = listOf(ScanItems.ReadCode, ScanItems.UploadCode)
            Box(Modifier.fillMaxSize()) {
                ModalBottomSheetLayout(sheetState = state, sheetShape = RoundedCornerShape(12.dp),sheetContent = {
                    Box(modifier = Modifier.defaultMinSize(minHeight = 1.dp)){
                        result?.let { BottomSheetResult(result = it) }
                    }
                }) {
                    ZxingCameraScan(pagerState.currentPage, state)
                    HorizontalPager(count = tabs.size, state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                        tabs[page].screen()
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp)) {
                        TabRounded(tabs = tabs, pagerState = pagerState, scope = scope)
                    }
                }
            }
        }
        is PermissionStatus.Denied -> {
            Box(modifier = Modifier.fillMaxSize()){
                Column(Modifier.align(Alignment.Center)) {
                    val textToShow = if ((cameraPermissionState.status as PermissionStatus.Denied).shouldShowRationale) {
                        "The camera is important for read qr code. Please grant the permission."
                    } else {
                        "Camera permission required for this feature to be available. " +
                                "Please grant the permission"
                    }
                    Text(textToShow, textAlign = TextAlign.Center)
                    TextButton(onClick = { cameraPermissionState.launchPermissionRequest() },
                        modifier = Modifier.align(CenterHorizontally)) {
                        Text("Activate")
                    }
                }
            }
        }
    }
}

/*
//Copied from https://stackoverflow.com/a/66763853. thank u @Sean

@androidx.camera.core.ExperimentalGetImage
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )


                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }

            val autoFocusPoint = SurfaceOrientedMeteringPointFactory(1f, 1f)
                .createPoint(.5f, .5f)
            val autoFocusAction = FocusMeteringAction.Builder(
                autoFocusPoint,
                FLAG_AF
            ).apply {
                setAutoCancelDuration(2, TimeUnit.SECONDS)
            }.build()

            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()


                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(previewView.width, previewView.height))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build().also {
                        it.setAnalyzer(ContextCompat.getMainExecutor(context), ZxingQrCodeAnalyzer(onQrCodesDetected = { result ->
                            println("New Barcode found: ${result.barcodeFormat.name}")
                            cameraProvider.unbindAll()
                        }))
                    }

                try {

                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, imageAnalysis ,preview
                    ).also {
                        it.cameraControl.startFocusAndMetering(autoFocusAction)
                    }

                } catch (exc: Exception) {
                    Log.e("CameraView.kt", "Use case binding failed", exc)
                }
            }, ContextCompat.getMainExecutor(context))

            previewView
        })
}
 */


//FUTURE UPDATE: REMOVE JOURNEYAPPS LIBRARY AND REPLACE WITH CAMERAX
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ZxingCameraScan(pagerIndex: Int, bottomSheetState: ModalBottomSheetState, scanViewModel: ScanViewModel = viewModel()) {
    var scanFlag by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()


    AndroidView(
        factory = { context ->
            DecoratedBarcodeView(context).apply {
                this.setStatusText("")
                this.viewFinder.setLaserVisibility(false)
            }
        },
        modifier = Modifier
    ) { decoratedBarcodeView ->
        if (pagerIndex == 1) {
            decoratedBarcodeView.barcodeView.stopDecoding()
        } else {
            val capture = CaptureManager(decoratedBarcodeView.context as Activity, decoratedBarcodeView)
            capture.initializeFromIntent((decoratedBarcodeView.context as Activity).intent, null)
            capture.decode()
            decoratedBarcodeView.decodeContinuous { decoratedResult ->
                if (scanFlag) {
                    return@decodeContinuous
                }
                println("scanFlag true")
                scanFlag = true
                decoratedResult.let {
                    scanViewModel.result.postValue(it)
                    decoratedBarcodeView.barcodeView.stopDecoding()
                    scanFlag = false
                    scope.launch {
                        bottomSheetState.show()
                    }
                }
            }

            if (!bottomSheetState.isVisible) decoratedBarcodeView.resume() else decoratedBarcodeView.pause()
        }
    }

}


