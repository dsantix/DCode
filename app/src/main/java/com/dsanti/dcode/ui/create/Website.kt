package com.dsanti.dcode.ui.create

import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dsanti.dcode.R
import com.dsanti.dcode.ui.isValidUrl
import com.dsanti.dcode.ui.legacySave
import com.dsanti.dcode.ui.saveImageInQ
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateWebsiteUrl(modifier: PaddingValues) {

    val scrollState = rememberScrollState(initial = 0)

    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val captureController = rememberCaptureController()

    var text by remember {
        mutableStateOf("https://")
    }

    val error by remember {
        mutableStateOf(text.isValidUrl())
    }

    val capturableBitmap = remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    val qrCodeColor = remember {
        mutableStateOf(Color.Black)
    }

    val qrCodeBackgroundColor = remember {
        mutableStateOf(Color.White)
    }

    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(sheetState = bottomSheetState, sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),sheetContent = {
        capturableBitmap.value?.let {
            AsyncImage(model = it.asAndroidBitmap(), contentDescription = null, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp))
        }

        Row(Modifier.align(Alignment.CenterHorizontally)) {

            Button(onClick = { capturableBitmap.value?.let {

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_STREAM, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        saveImageInQ(it.asAndroidBitmap(), context)
                    else
                        legacySave(it.asAndroidBitmap(), context)
                    )
                    type = "image/jpeg"
                }
                Toast.makeText(context, R.string.qrcode_saved, Toast.LENGTH_SHORT).show()
                context.startActivity(Intent.createChooser(shareIntent, "Share"))
            } }) {
                Text(text = stringResource(id = R.string.action_share))
            }

            Button(onClick = {
                capturableBitmap.value?.let {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        saveImageInQ(it.asAndroidBitmap(), context)
                    else
                        legacySave(it.asAndroidBitmap(), context)

                    Toast.makeText(context, R.string.qrcode_saved, Toast.LENGTH_SHORT).show()
                }}, modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(text = stringResource(id = R.string.action_save))
            }
        }
    }) {
        Column(
            Modifier
                .padding(modifier)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)) {


            TextField(value = text, onValueChange = {text = it},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {if (text.isNotEmpty()){
                    FaIcon(faIcon = FaIcons.TimesCircle, modifier = Modifier.clickable {
                        text = ""
                    })
                } },
                isError = error,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))
            if (text.isNotEmpty() && !text.isValidUrl()) Text(text = stringResource(id = R.string.invalid_url), modifier = Modifier.padding(horizontal = 8.dp), color = MaterialTheme.colorScheme.error)

            QRCodeSettings(qrCodeBackgroundColor, qrCodeColor, text, captureController, capturableBitmap)

            Button(onClick = {
                if (text.isNotEmpty() && text.isValidUrl()){
                    scope.launch {
                        captureController.capture()
                        bottomSheetState.show()
                    }
                }else{
                    Toast.makeText(context, R.string.enter_valid_url, Toast.LENGTH_LONG).show()
                }
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp)) {
                Text(text = stringResource(id = R.string.generate_qrcode))
            }
        }
    }

}