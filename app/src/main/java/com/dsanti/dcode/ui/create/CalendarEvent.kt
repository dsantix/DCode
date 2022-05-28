package com.dsanti.dcode.ui.create

import DateAndTimePicker
import TimePicker
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dsanti.dcode.R
import com.dsanti.dcode.data.qrGenerator
import com.dsanti.dcode.ui.isValidUrl
import com.dsanti.dcode.ui.legacySave
import com.dsanti.dcode.ui.saveImageInQ
import com.dsanti.dcode.ui.theme.AppTypography
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateCalendarEvent(modifier: PaddingValues) {

    val scrollState = rememberScrollState(initial = 0)

    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val captureController = rememberCaptureController()

    var title by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var data by remember {
        mutableStateOf("")
    }

    var startCalendar by remember {
        mutableStateOf(Calendar.getInstance().time)
    }

    var endCalendar by remember {
        mutableStateOf(Calendar.getInstance().time)
    }

    var startDate by remember {
        mutableStateOf(SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(startCalendar))
    }

    var endDate by remember {
        mutableStateOf(SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(endCalendar))
    }

    var startTime by remember {
        mutableStateOf("${Calendar.getInstance().get(Calendar.HOUR_OF_DAY)}:${Calendar.getInstance().get(Calendar.MINUTE)}")
    }

    var endTime by remember {
        mutableStateOf("${Calendar.getInstance().get(Calendar.HOUR_OF_DAY)}:${Calendar.getInstance().get(Calendar.MINUTE)}")
    }


    var qrCodeBitmap by remember {
        mutableStateOf<Bitmap?>(null)
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

    var showStartDate by remember {
        mutableStateOf(false)
    }

    var showEndDate by remember {
        mutableStateOf(false)
    }

    var showStartTime by remember {
        mutableStateOf(false)
    }

    var showEndTime by remember {
        mutableStateOf(false)
    }

    if (showStartDate) {
        DateAndTimePicker(onDateSelected = {
            startCalendar = it
            startDate = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(it)
        }, onDismissRequest = {
            showStartDate = false
        }, showTime = true)
    }

    if (showEndDate) {
        DateAndTimePicker(onDateSelected = {
            endCalendar = it
            endDate = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(it)
        }, onDismissRequest = {
            showEndDate = false
        }, showTime = true)
    }

    if (showStartTime) {
        TimePicker(context = context, currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY), currentMinute = Calendar.getInstance().get(Calendar.MINUTE), onTimeSelected = {
            startTime = it
        }, onDismissRequest = {
            showStartTime = !it
        })
    }

    if (showEndTime) {
        TimePicker(context = context, currentHour = Calendar.getInstance().get(Calendar.MINUTE), currentMinute = Calendar.getInstance().get(Calendar.MINUTE), onTimeSelected = {
            endTime = it
        }, onDismissRequest = {
            showEndTime = !it
        })
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


            Text(text = stringResource(id = R.string.all_day_event), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            Switch(checked = checked, onCheckedChange = { checked = it }, thumbContent = icon, modifier = Modifier.padding(horizontal = 8.dp))

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

            Text(text = stringResource(id = R.string.vevent_startDate), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = startDate, onValueChange = {},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        showStartDate = true
                    },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                    disabledLabelColor =  MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium)
                ),
                enabled = false,
                readOnly = true,
                singleLine = true)

            AnimatedVisibility(visible = !checked) {
                Column {
                    Text(text = stringResource(id = R.string.startTime), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

                    TextField(value = startTime, onValueChange = {},
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable {
                                showStartTime = true
                            },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                            disabledLabelColor =  MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium)
                        ),
                        enabled = false,
                        readOnly = true,
                        singleLine = true)
                }
            }

            Text(text = stringResource(id = R.string.vevent_endDate), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = endDate, onValueChange = {},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        showEndDate = true
                    },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                    disabledLabelColor =  MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium)
                ),
                enabled = false,
                readOnly = true,
                singleLine = true)


            AnimatedVisibility(visible = !checked) {
                Column {
                    Text(text = stringResource(id = R.string.endTime), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

                    TextField(value = endTime, onValueChange = {},
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable {
                                showEndTime = true
                            },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                            disabledLabelColor =  MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium)
                        ),
                        enabled = false,
                        readOnly = true,
                        singleLine = true)
                }
            }

            Text(text = stringResource(id = R.string.vevent_location), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = location, onValueChange = {location = it},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {if (location.isNotEmpty()){
                    FaIcon(faIcon = FaIcons.TimesCircle, modifier = Modifier.clickable {
                        location = ""
                    })
                } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))


            Text(text = stringResource(id = R.string.vevent_description), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleSmall)

            TextField(value = description, onValueChange = {description = it},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {if (description.isNotEmpty()){
                    FaIcon(faIcon = FaIcons.TimesCircle, modifier = Modifier.clickable {
                        description = ""
                    })
                } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()} ))

            QRCodeSettings(qrCodeBackgroundColor, qrCodeColor, qrCodeBitmap, captureController, capturableBitmap)

            data = buildString {
                append("BEGIN:VEVENT\n")
                append("SUMMARY:$title\n")
                if (checked) append("DTSTART;VALUE=DATE:${SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(startCalendar)}")
                    else append("DTSTART:${SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()).format(startCalendar)}")
                if (checked) append("DTEND;VALUE=DATE:${SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(endCalendar)}")
                    else append("DTEND:${SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()).format(endCalendar)}")
                if (location.isNotEmpty()) append("LOCATION:$location")
                if (description.isNotEmpty()) append("DESCRIPTION:$description")
                append("END:VEVENT")
            }

            Button(onClick = {
                if (title.isNotEmpty() && data.isNotEmpty()){
                    qrCodeBitmap = qrGenerator(data, 512, 512, qrCodeColor.value.toArgb(), qrCodeBackgroundColor.value.toArgb())

                    scope.launch {
                        delay(300)

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
        }
    }

}