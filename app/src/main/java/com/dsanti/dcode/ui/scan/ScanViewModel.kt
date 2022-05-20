package com.dsanti.dcode.ui.scan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.journeyapps.barcodescanner.BarcodeResult

class ScanViewModel : ViewModel() {
    val result: MutableLiveData<BarcodeResult?> = MutableLiveData()
}