package com.dsanti.dcode.ui.create

import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.dsanti.dcode.ui.components.AdBannerView
import com.dsanti.dcode.ui.theme.AppTypography
import com.dsanti.dcode.utils.legacySave
import com.dsanti.dcode.utils.saveImageInQ
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateAppStore(modifier: PaddingValues) {

    val scrollState = rememberScrollState(initial = 0)

    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val captureController = rememberCaptureController()

    var text by remember {
        mutableStateOf("")
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

    var expanded by remember { mutableStateOf(false) }

    val options = listOf(R.string.qr_app_store, R.string.play_store)

    var selectedOptionText by remember { mutableStateOf(options[0]) }


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
                label = {
                    if (selectedOptionText == R.string.play_store) Text(text = stringResource(id = R.string.example_playstore)) else Text(
                        text = stringResource(id = R.string.example_appstore)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))

            Text(text = stringResource(id = R.string.store), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {

                TextField(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    readOnly = true,
                    value = stringResource(selectedOptionText),
                    shape = RoundedCornerShape(8.dp),
                    onValueChange = {},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(stringResource(id = selectionOption)) },
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }

           val data = if (selectedOptionText == R.string.play_store) "https://play.google.com/store/apps/details?id=$text" else "https://itunes.apple.com/us/app/$text"

            QRCodeSettings(qrCodeBackgroundColor, qrCodeColor, data, captureController, capturableBitmap)

            OutlinedButton(onClick = {
                if (text.isNotEmpty() && data.isNotEmpty()){
                    scope.launch {

                        captureController.capture()
                        bottomSheetState.show()
                    }
                }else{
                    Toast.makeText(context, R.string.enter_validtext, Toast.LENGTH_LONG).show()
                }
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp)) {
                Text(text = stringResource(id = R.string.generate_qrcode))
            }

            AdBannerView(false)

        }
    }

}