package com.dsanti.dcode.ui.scan

import android.speech.tts.TextToSpeech
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.Result
import com.journeyapps.barcodescanner.BarcodeResult
import kotlinx.coroutines.launch

class ScanViewModel : ViewModel() {
    val result: MutableLiveData<BarcodeResult?> = MutableLiveData()
    val resultUpload : MutableLiveData<Result> = MutableLiveData()


    private lateinit var textToSpeechEngine: TextToSpeech

    fun initial(
        engine: TextToSpeech
    ) = viewModelScope.launch {
        textToSpeechEngine = engine
    }


    fun speak(text: String) = viewModelScope.launch{
        textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}