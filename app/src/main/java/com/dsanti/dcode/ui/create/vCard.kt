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
import com.dsanti.dcode.ui.components.AdBannerView
import com.dsanti.dcode.ui.theme.AppTypography
import com.dsanti.dcode.utils.isValidEmail
import com.dsanti.dcode.utils.isValidPhone
import com.dsanti.dcode.utils.isValidUrl
import com.dsanti.dcode.utils.legacySave
import com.dsanti.dcode.utils.saveImageInQ
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateVCard(modifier: PaddingValues) {

    val scrollState = rememberScrollState(initial = 0)

    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val captureController = rememberCaptureController()

    var name by remember {
        mutableStateOf("")
    }

    var title by remember {
        mutableStateOf("")
    }

    var company by remember {
        mutableStateOf("")
    }

    var phoneNumber by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var address by remember {
        mutableStateOf("")
    }

    var website by remember {
        mutableStateOf("")
    }

    var note by remember {
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

            Text(text = stringResource(id = R.string.vcard_name), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = name, onValueChange = {name = it},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {if (name.isNotEmpty()){
                    FaIcon(faIcon = FaIcons.TimesCircle, modifier = Modifier.clickable {
                        name = ""
                    })
                } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))

            Text(text = stringResource(id = R.string.vcard_org), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = company, onValueChange = {company = it},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {if (company.isNotEmpty()){
                    FaIcon(faIcon = FaIcons.TimesCircle, modifier = Modifier.clickable {
                        company = ""
                    })
                } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))


            Text(text = stringResource(id = R.string.vevent_title), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = title, onValueChange = {title = it},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {if (title.isNotEmpty()){
                    FaIcon(faIcon = FaIcons.TimesCircle, modifier = Modifier.clickable {
                        title = ""
                    })
                } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))


            Text(text = stringResource(id = R.string.qr_number), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = phoneNumber, onValueChange = {phoneNumber = it},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {if (phoneNumber.isNotEmpty()){
                    FaIcon(faIcon = FaIcons.TimesCircle, modifier = Modifier.clickable {
                        phoneNumber = ""
                    })
                } },
                isError = phoneNumber.isNotEmpty() && !phoneNumber.isValidPhone(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))

            if (phoneNumber.isNotEmpty() && !phoneNumber.isValidPhone()) Text(text = stringResource(id = R.string.enter_a_validnumber), modifier = Modifier.padding(horizontal = 8.dp), color = MaterialTheme.colorScheme.error)


            Text(text = stringResource(id = R.string.qr_email), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = email, onValueChange = {email = it},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {if (email.isNotEmpty()){
                    FaIcon(faIcon = FaIcons.TimesCircle, modifier = Modifier.clickable {
                        email = ""
                    })
                } },
                isError = email.isNotEmpty() && !email.isValidEmail(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))

            if (email.isNotEmpty() && !email.isValidEmail()) Text(text = stringResource(id = R.string.enter_valid_email), modifier = Modifier.padding(horizontal = 8.dp), color = MaterialTheme.colorScheme.error)

            Text(text = stringResource(id = R.string.vcard_address), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = address, onValueChange = {address = it},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {if (address.isNotEmpty()){
                    FaIcon(faIcon = FaIcons.TimesCircle, modifier = Modifier.clickable {
                        address = ""
                    })
                } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))

            Text(text = stringResource(id = R.string.vcard_url), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = website, onValueChange = {website = it},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {if (website.isNotEmpty()){
                    FaIcon(faIcon = FaIcons.TimesCircle, modifier = Modifier.clickable {
                        website = ""
                    })
                } },
                isError = website.isNotEmpty() && !website.isValidUrl(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))
            if (website.isNotEmpty() && !website.isValidUrl()) Text(text = stringResource(id = R.string.enter_valid_url), modifier = Modifier.padding(horizontal = 8.dp), color = MaterialTheme.colorScheme.error)

            Text(text = stringResource(id = R.string.vcard_note), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = note, onValueChange = {note = it},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {if (note.isNotEmpty()){
                    FaIcon(faIcon = FaIcons.TimesCircle, modifier = Modifier.clickable {
                        note = ""
                    })
                } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))

            QRCodeSettings(qrCodeBackgroundColor, qrCodeColor, buildString {
                append("BEGIN:VCARD\n")
                append("VERSION:3.0\n")
                if (name.isNotEmpty()) append("N:$name\n")
                if (company.isNotEmpty()) append("ORG:$company\n")
                if (title.isNotEmpty()) append("TITLE:$title\n")
                if (phoneNumber.isNotEmpty() && phoneNumber.isValidPhone()) append("TEL:$phoneNumber\n")
                if (email.isNotEmpty() && email.isValidEmail()) append("EMAIL:$email\n")
                if (address.isNotEmpty()) append("ADR:$address\n")
                if (website.isNotEmpty() && website.isValidUrl()) append("URL:$website\n")
                if (note.isNotEmpty()) append("NOTE:$note\n")
                append("END:VCARD\n")
            }, captureController, capturableBitmap)

            OutlinedButton(onClick = {
                scope.launch {
                    captureController.capture()
                    bottomSheetState.show()
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp)) {
                Text(text = stringResource(id = R.string.generate_qrcode))
            }

            AdBannerView(false)
        }
    }

}