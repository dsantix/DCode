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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
fun CreateWifi(modifier: PaddingValues) {

    val scrollState = rememberScrollState(initial = 0)

    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val captureController = rememberCaptureController()

    var textSSID by remember {
        mutableStateOf("")
    }

    var textPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

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

    var checked by remember { mutableStateOf(false) }

    val icon: (@Composable () -> Unit)? = if (checked) {
        {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }

    val options = listOf(R.string.wep, R.string.wpa_wpa2, R.string.no_encryption)

    var selectedOptionText by remember { mutableStateOf(options[1]) }

    val textEncryption = stringResource(id = selectedOptionText)


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

            Text(text = stringResource(id = R.string.wifi_name), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = textSSID, onValueChange = {textSSID = it},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {if (textSSID.isNotEmpty()){
                    FaIcon(faIcon = FaIcons.TimesCircle, modifier = Modifier.clickable {
                        textSSID = ""
                    })
                } },
                isError = textSSID.isEmpty(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))

            Text(text = stringResource(id = R.string.password), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = textPassword, onValueChange = {textPassword = it},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                    }
                },
                singleLine = false,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))

            Text(text = stringResource(id = R.string.networktype), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

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

            Text(text = stringResource(id = R.string.isHidden), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)


            Switch(checked = checked, onCheckedChange = { checked = it }, thumbContent = icon, modifier = Modifier.padding(horizontal = 8.dp))


            QRCodeSettings(qrCodeBackgroundColor, qrCodeColor, buildString {
                append("WIFI:S:$textSSID;")
                if (textPassword.isNotEmpty()) append("P:$textPassword;")
                append("T:$textEncryption;")
                append("H:$checked;")
                append(";")
            }, captureController, capturableBitmap)


            OutlinedButton(onClick = {
                if (textSSID.isNotEmpty()){
                    scope.launch {

                        captureController.capture()
                        bottomSheetState.show()
                    }
                }else{
                    Toast.makeText(context, R.string.enter_validssid, Toast.LENGTH_LONG).show()
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