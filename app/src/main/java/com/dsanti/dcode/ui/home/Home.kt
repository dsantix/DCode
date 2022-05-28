package com.dsanti.dcode.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.dsanti.dcode.R
import com.dsanti.dcode.data.Update
import com.dsanti.dcode.ui.settings.updates
import com.dsanti.dcode.ui.theme.AppTypography
import com.dsanti.dcode.ui.theme.DCodeTheme


@Composable
fun Home(isUpdated:MutableState<Boolean>, navController: NavController){
    val shapeColor = MaterialTheme.colorScheme.primary

    if (isUpdated.value) ChangelogDialog(openDialog = isUpdated)

    Box {

        Column {
            Icon(painter = painterResource(id = R.drawable.ic_qr_create_icon), contentDescription = stringResource(
                id = R.string.contentd_dcode_icon
            ), tint = shapeColor, modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(text = stringResource(id = R.string.app_name), modifier = Modifier
                .padding(top = 1.dp)
                .align(Alignment.CenterHorizontally))
            Content(navController)
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
            .wrapContentSize(Alignment.TopStart)) {


            Box(modifier = Modifier
                .size(54.dp)
                .rotate(45F)
                .border(BorderStroke(2.dp, shapeColor))
            )

            Spacer(modifier = Modifier.width(40.dp))

            Box(modifier = Modifier
                .size(26.dp)
                .rotate(45F)
                .clip(RoundedCornerShape(2.dp))
                .background(shapeColor))
        }

        Box(modifier = Modifier
            .size(54.dp)
            .rotate(45F)
            .border(BorderStroke(2.dp, shapeColor))
            .align(Alignment.TopEnd)
        )
    }

}

@Composable
fun Content(navController: NavController) {
    Spacer(modifier = Modifier.height(50.dp))
    //SearchTextField()
    Text(text = stringResource(id = R.string.home_create),
        style = AppTypography.displaySmall
    )
    Spacer(modifier = Modifier.height(8.dp))
    GridListContentScrolling(navController)
}


@Composable
fun SearchTextField() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp)) {
        Text(text = stringResource(id = R.string.home_create),
            style = AppTypography.displaySmall, modifier = Modifier.padding(horizontal = 8.dp)
        )

        var text by remember { mutableStateOf("") }
        val focusManager = LocalFocusManager.current
        TextField(value = text, onValueChange = {text = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()})
        )
    }
}

@Composable
fun ChangelogDialog(openDialog: MutableState<Boolean>) {
    Dialog(onDismissRequest = { openDialog.value = false}) {
        ChangelogDialogItem(update = updates.last(), openDialog = openDialog)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangelogDialogItem(update: Update, openDialog: MutableState<Boolean>) {
    Card(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
        Text(text = stringResource(id = R.string.app_updated), style = AppTypography.titleLarge, modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(), textAlign = TextAlign.Center)
        Column(Modifier.fillMaxWidth()) {

            Text(text = update.date, Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Normal)

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp)) {

                //Version
                Text(text = "v${update.version}",
                    Modifier
                        .align(Alignment.CenterVertically), fontWeight = FontWeight.ExtraBold)
                Card(Modifier.padding(horizontal = 6.dp), colors = CardDefaults.cardColors(containerColor = Color.Black), shape = RoundedCornerShape(8.dp)) {
                    Text(text = update.version_label, modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.End), color = Color.White, fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold
                    )
                }
            }

            if (update.whatsNew.isNotEmpty()){
                Text(text = "What's new", Modifier.padding(horizontal = 8.dp, vertical = 8.dp), style = AppTypography.titleMedium, fontWeight = FontWeight.Bold)

                update.whatsNew.onEach {
                    Text(text = "⚬ $it", modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp))
                }
            }

            if (update.fixed.isNotEmpty()){
                Text(text = "Fixed", Modifier.padding(horizontal = 8.dp, vertical = 8.dp), style = AppTypography.titleMedium, fontWeight = FontWeight.Bold)

                update.fixed.onEach {
                    Text(text = "⚬ $it", modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp))
                }
            }

            if (update.improved.isNotEmpty()){
                Text(text = "Improved", Modifier.padding(horizontal = 8.dp, vertical = 8.dp), style = AppTypography.titleMedium, fontWeight = FontWeight.Bold)

                update.improved.onEach {
                    Text(text = "⚬ $it", modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp))
                }
            }


        }
        Button(onClick = { openDialog.value = false }, modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(horizontal = 8.dp)) {
            Text(text = stringResource(id = R.string.ok))
        }
    }
}

@Preview
@Composable
fun ChangelogItemDialogPreview(){
    val state = remember {
        mutableStateOf(false)
    }
    ChangelogDialogItem(update = updates.last(), state)
}

@Preview
@Composable
fun PreviewHome(){
    DCodeTheme {
        PreviewHome()
    }
}