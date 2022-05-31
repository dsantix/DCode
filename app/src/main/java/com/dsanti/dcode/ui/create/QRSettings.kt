package com.dsanti.dcode.ui.create

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.dsanti.dcode.R
import com.dsanti.dcode.data.qrGenerator
import com.dsanti.dcode.ui.generateQRCode
import com.dsanti.dcode.ui.isValidUrl
import com.dsanti.dcode.ui.legacySave
import com.dsanti.dcode.ui.saveImageInQ
import com.dsanti.dcode.ui.theme.AppTypography
import com.google.zxing.BarcodeFormat
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.journeyapps.barcodescanner.BarcodeEncoder
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.CaptureController
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

val colorsList = listOf(ColorsPick.Black, ColorsPick.White, ColorsPick.Red, ColorsPick.Green, ColorsPick.Blue, ColorsPick.Yellow)

@Composable
fun QRCodeSettings(qrCodeBackgroundColor: MutableState<Color>, qrCodeColor: MutableState<Color>, qrCodeData:String, capturableController: CaptureController, capturableBitmap: MutableState<ImageBitmap?>) {

    val cardContentColor = remember {
        mutableStateOf(Color.Black)
    }

    val cardBackgroundChange = remember {
        mutableStateOf(false)
    }

    val textColorChange = remember {
        mutableStateOf(false)
    }

    val qrBackgroundColorChange = remember {
        mutableStateOf(false)
    }

    val qrCodeColorChange = remember {
        mutableStateOf(false)
    }

    val textColor = remember {
        mutableStateOf(Color.White)
    }

    val previewWithCard = remember {
        mutableStateOf(true)
    }

    val previewBottomTextWithCard = remember {
        mutableStateOf(false)
    }

    val previewOnlyQRCode = remember {
        mutableStateOf(false)
    }

    val bottomText by remember {
        mutableStateOf("Scan me")
    }

    val cardShapeExpanded = remember {
        mutableStateOf(false)
    }

    val cardFrameExpanded = remember {
        mutableStateOf(false)
    }

    val dialogState = remember {
        mutableStateOf(false)
    }

    if (dialogState.value) ColorDialog(dialogState = dialogState, onSaveClick = {
        when {
            textColorChange.value -> textColor.value = it
            cardBackgroundChange.value -> cardContentColor.value = it
            qrBackgroundColorChange.value -> qrCodeBackgroundColor.value = it
            qrCodeColorChange.value -> qrCodeColor.value = it
        }
    })


    Column {

        Text(text = stringResource(id = R.string.livePreview), modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 8.dp))


        Capturable(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(vertical = 8.dp), controller = capturableController, onCaptured = { bitmap, _ ->
            if (bitmap != null){
                capturableBitmap.value = bitmap
            }
        }) {
            LivePreviewCard(qrCodeData,previewWithCard, previewBottomTextWithCard, previewOnlyQRCode, bottomText, cardContentColor, textColor, qrCodeColor, qrCodeBackgroundColor)
        }


        FrameCard(
            cardFrameExpanded = cardFrameExpanded,
            previewWithCard = previewWithCard,
            previewOnlyQRCode = previewOnlyQRCode,
            previewBottomTextWithCard = previewBottomTextWithCard,
            bottomText = bottomText
        )

        ShapeAndColorCard(
            cardContentColor = cardContentColor,
            cardShapeExpanded = cardShapeExpanded,
            dialogState = dialogState,
            cardBackgroundChange = cardBackgroundChange,
            textColorChange = textColorChange,
            textColor = textColor,
            qrCodeColor = qrCodeColor,
            qrCodeBackgroundColor = qrCodeBackgroundColor,
            qrBackgroundColorChange = qrBackgroundColorChange,
            qrCodeColorChange = qrCodeColorChange,
            previewBottomTextWithCard = previewBottomTextWithCard,
            previewWithCard = previewWithCard
        )

        
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorDialog(dialogState:MutableState<Boolean>, onSaveClick : (Color) -> Unit) {
    
    var red by remember {
        mutableStateOf(0f)
    }

    var blue by remember {
        mutableStateOf(0f)
    }

    var green by remember {
        mutableStateOf(0f)
    }

    val color = Color(
        red = red.toInt(),
        green = green.toInt(),
        blue = blue.toInt(),
        alpha = 255
    )



    Dialog(onDismissRequest = { dialogState.value = false }) {
        Card {
            Column {
                Text(text = stringResource(id = R.string.choose_a_color), fontWeight = FontWeight.Bold,modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp))

                Text(text = stringResource(id = R.string.red), modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))
                
                Slider(value = red, onValueChange = {red = it}, valueRange = 0f..255f, onValueChangeFinished = {}, modifier = Modifier.padding(vertical = 2.dp))

                Text(text = stringResource(id = R.string.green), modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))

                Slider(value = green, onValueChange = {green = it}, valueRange = 0f..255f, onValueChangeFinished = {}, modifier = Modifier.padding(vertical = 2.dp))

                Text(text = stringResource(id = R.string.blue), modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))

                Slider(value = blue, onValueChange = {blue = it}, valueRange = 0f..255f, onValueChangeFinished = {}, modifier = Modifier.padding(vertical = 2.dp))
                
                
                Text(text = stringResource(id = R.string.result), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp))

                Surface(color = color, modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(horizontal = 8.dp), shape = RoundedCornerShape(50)) {
                }

                Row(
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp)) {
                    Button(onClick = { dialogState.value = false }, modifier = Modifier.padding(8.dp)) {
                        Text(text = stringResource(id = R.string.action_cancel))
                    }

                    Button(onClick = {
                        dialogState.value = false
                        onSaveClick(color)
                    }, modifier = Modifier.padding(8.dp)) {
                        Text(text = stringResource(id = R.string.action_save))
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrameCard(cardFrameExpanded:MutableState<Boolean>, previewWithCard: MutableState<Boolean>, previewOnlyQRCode: MutableState<Boolean>, previewBottomTextWithCard: MutableState<Boolean>, bottomText: String) {


    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .animateContentSize(animationSpec = tween(300, easing = LinearOutSlowInEasing))
            .clickable {
                cardFrameExpanded.value = cardFrameExpanded.value == false
            }, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        if (cardFrameExpanded.value){
            Column{
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                    Text(text = stringResource(id = R.string.frame), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleLarge, color = Color.Black)
                    Icon(imageVector = Icons.Rounded.ArrowDropUp, contentDescription = null, modifier = Modifier.align(Alignment.CenterVertically))
                }

                Row(Modifier.align(Alignment.CenterHorizontally)) {

                    Button(onClick = { previewWithCard.value = true
                        previewOnlyQRCode.value = false
                        previewBottomTextWithCard.value = false}, shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .width(65.dp)
                            .height(65.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
                        contentPadding = PaddingValues(4.dp)) {
                        Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(4.dp)) {
                            Column {
                                Icon(painter = painterResource(id = R.drawable.ic_qr_code_icon), contentDescription = null, modifier = Modifier
                                    .width(35.dp)
                                    .height(35.dp)
                                    .padding(start = 4.dp, end = 4.dp, top = 1.dp)
                                    .align(Alignment.CenterHorizontally), tint = Color.Black)

                                Text(text = bottomText, color = Color.Black, modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(bottom = 4.dp, start = 4.dp, end = 4.dp), fontSize = 5.sp, textAlign = TextAlign.Center)

                            }
                        }
                    }

                    Button(onClick = { previewWithCard.value = false
                        previewOnlyQRCode.value = false
                        previewBottomTextWithCard.value = true },
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .width(65.dp)
                            .height(65.dp),
                        contentPadding = PaddingValues(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)) {
                        Column {
                            Icon(painter = painterResource(id = R.drawable.ic_qr_code_icon), contentDescription = null, modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .align(Alignment.CenterHorizontally), tint = Color.Black)

                            Card(Modifier.padding(top = 4.dp)){
                                Text(text = bottomText, color = Color.Black, modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(2.dp), fontSize = 8.sp)
                            }
                        }
                    }

                    Button(onClick = { previewWithCard.value = false
                        previewOnlyQRCode.value = true
                        previewBottomTextWithCard.value = false },
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .width(65.dp)
                            .height(65.dp),
                        contentPadding = PaddingValues(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)) {
                        Icon(painter = painterResource(id = R.drawable.ic_qr_code_icon), contentDescription = null, modifier = Modifier
                            .width(30.dp)
                            .height(30.dp), tint = Color.Black)
                    }
                }
            }

        }else{
            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(id = R.string.frame), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleLarge)
                Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShapeAndColorCard(
    cardContentColor: MutableState<Color>,
    cardShapeExpanded: MutableState<Boolean>,
    previewWithCard: MutableState<Boolean>,
    previewBottomTextWithCard: MutableState<Boolean>,
    dialogState: MutableState<Boolean>,
    cardBackgroundChange: MutableState<Boolean>,
    textColorChange: MutableState<Boolean>,
    qrCodeColorChange: MutableState<Boolean>,
    qrBackgroundColorChange: MutableState<Boolean>,
    textColor: MutableState<Color>,
    qrCodeColor: MutableState<Color>,
    qrCodeBackgroundColor : MutableState<Color>
) {
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .animateContentSize(animationSpec = tween(300, easing = LinearOutSlowInEasing))
            .clickable {
                cardShapeExpanded.value = cardShapeExpanded.value == false
            }, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        if (cardShapeExpanded.value){
            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(id = R.string.shapeAndColor), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleLarge)
                Icon(imageVector = Icons.Rounded.ArrowDropUp, contentDescription = null)
            }

            //Color
            Column {

                AnimatedVisibility(visible = previewWithCard.value || previewBottomTextWithCard.value) {
                    Column {
                        Text(text = stringResource(id = R.string.cardBackground), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp))

                        LazyRow {
                            items(colorsList.size){ index ->

                                Button(onClick = { cardContentColor.value = colorsList[index].color }, shape = RoundedCornerShape(100),modifier = Modifier.padding(horizontal = 8.dp), colors = ButtonDefaults.buttonColors(
                                    containerColor = colorsList[index].color
                                )) {

                                }
                            }

                            //Open dialog
                            item {
                                Button(onClick = { dialogState.value = true
                                    cardBackgroundChange.value = true
                                    textColorChange.value = false
                                    qrBackgroundColorChange.value = false
                                    qrCodeColorChange.value = false }, shape = RoundedCornerShape(100),modifier = Modifier.padding(horizontal = 8.dp)) {
                                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                                }
                            }

                        }

                        Text(text = stringResource(id = R.string.textColor), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp))

                        LazyRow {
                            items(colorsList.size){ index ->
                                Button(onClick = { textColor.value = colorsList[index].color }, shape = RoundedCornerShape(100),modifier = Modifier.padding(horizontal = 8.dp),colors = ButtonDefaults.buttonColors(
                                    containerColor = colorsList[index].color
                                )) {

                                }
                            }

                            //Open dialog
                            item {
                                Button(onClick = {
                                    dialogState.value = true
                                    cardBackgroundChange.value = false
                                    textColorChange.value = true
                                    qrBackgroundColorChange.value = false
                                    qrCodeColorChange.value = false
                                }, shape = RoundedCornerShape(100),modifier = Modifier.padding(horizontal = 8.dp)) {
                                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                                }
                            }

                        }
                    }
                }

                Text(text = stringResource(id = R.string.qrcode_color), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp))

                LazyRow {
                    items(colorsList.size){ index ->
                        Button(onClick = { qrCodeColor.value = colorsList[index].color }, shape = RoundedCornerShape(100),modifier = Modifier.padding(horizontal = 8.dp),colors = ButtonDefaults.buttonColors(
                            containerColor = colorsList[index].color
                        )) {

                        }
                    }

                    //Open dialog
                    item {
                        Button(onClick = {
                            dialogState.value = true
                            cardBackgroundChange.value = false
                            textColorChange.value = false
                            qrBackgroundColorChange.value = false
                            qrCodeColorChange.value = true
                        }, shape = RoundedCornerShape(100),modifier = Modifier.padding(horizontal = 8.dp)) {
                            Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                        }
                    }

                }

                Text(text = stringResource(id = R.string.qrbackground_color), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp))

                LazyRow {
                    items(colorsList.size){ index ->
                        Button(onClick = { qrCodeBackgroundColor.value = colorsList[index].color }, shape = RoundedCornerShape(100),modifier = Modifier.padding(horizontal = 8.dp),colors = ButtonDefaults.buttonColors(
                            containerColor = colorsList[index].color
                        )) {

                        }
                    }

                    //Open dialog
                    item {
                        Button(onClick = {
                            dialogState.value = true
                            cardBackgroundChange.value = false
                            textColorChange.value = false
                            qrBackgroundColorChange.value = true
                            qrCodeColorChange.value = false
                        }, shape = RoundedCornerShape(100),modifier = Modifier.padding(horizontal = 8.dp)) {
                            Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                        }
                    }

                }
            }

        }else{
            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(id = R.string.shapeAndColor), modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), style = AppTypography.titleLarge)
                Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivePreviewCard(qrCodeData:String, previewWithCard:MutableState<Boolean>, previewBottomTextWithCard:MutableState<Boolean>, previewOnlyQRCode:MutableState<Boolean>, bottomText:String, cardContentColor:MutableState<Color>, textColor: MutableState<Color>, qrCodeColor: MutableState<Color>, qrCodeBackgroundColor: MutableState<Color>) {

    when {
        previewWithCard.value -> {
            Card(colors = CardDefaults.cardColors(
                containerColor = cardContentColor.value
            )) {
                Column {
                    if (qrCodeData.isEmpty()){
                        Box(modifier = Modifier
                            .size(100.dp)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 1f)))
                    }else{
                        Image(bitmap = ImageBitmap.generateQRCode(qrCodeData, color = qrCodeColor.value.toArgb(), backgroundColor = qrCodeBackgroundColor.value.toArgb()),
                            contentDescription = null,
                            modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(24.dp)
                        )
                    }

                    Text(text = bottomText, color = textColor.value, modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp), fontWeight = FontWeight.Bold, style = AppTypography.titleLarge)
                }
            }
        }

        previewBottomTextWithCard.value -> {
            Column {
                if (qrCodeData.isEmpty()){
                    Box(modifier = Modifier
                        .size(100.dp)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 1f)))
                }else{
                    Image(bitmap = ImageBitmap.generateQRCode(qrCodeData, color = qrCodeColor.value.toArgb(), backgroundColor = qrCodeBackgroundColor.value.toArgb()),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(24.dp)
                    )
                }

                Card(Modifier.align(Alignment.CenterHorizontally), colors = CardDefaults.cardColors(containerColor = cardContentColor.value)) {
                    Text(text = bottomText, color = textColor.value,modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(4.dp), fontWeight = FontWeight.Bold, style = AppTypography.titleLarge)
                }
            }
        }

        previewOnlyQRCode.value -> {
            if (qrCodeData.isEmpty()){
                Box(modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 1f)))
            }else{
                Image(bitmap = ImageBitmap.generateQRCode(qrCodeData, color = qrCodeColor.value.toArgb(), backgroundColor = qrCodeBackgroundColor.value.toArgb()),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }

        }

    }
}

sealed class ColorsPick(val color: Color){
    object Black : ColorsPick(Color(0xFF000000))
    object White : ColorsPick(Color(0xFFFFFFFF))
    object Blue : ColorsPick(Color(0xFF0000FF))
    object Red : ColorsPick(Color(0xFFFF0000))
    object Green : ColorsPick(Color(0xFF008000))
    object Yellow : ColorsPick(Color(0xFFFFFF00))
}