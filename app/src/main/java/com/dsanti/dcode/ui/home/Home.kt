package com.dsanti.dcode.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.*
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
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dsanti.dcode.R
import com.dsanti.dcode.ui.ComposableFun
import com.dsanti.dcode.ui.pagerTabIndicatorOffset
import com.dsanti.dcode.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@Composable
fun Home(){
    val shapeColor = MaterialTheme.colorScheme.primary


    Box {

        Column {
            Icon(painter = painterResource(id = R.drawable.ic_qr_create_icon), contentDescription = stringResource(
                id = R.string.contentd_dcode_icon
            ), tint = shapeColor, modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(text = stringResource(id = R.string.app_name), modifier = Modifier.padding(top = 1.dp).align(Alignment.CenterHorizontally))
            Content()
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
fun Content() {
    Spacer(modifier = Modifier.height(50.dp))
    SearchTextField()
    Spacer(modifier = Modifier.height(8.dp))
    GridListContentScrolling()
}


@Composable
fun SearchTextField() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp)) {
        Text(text = stringResource(id = R.string.home_create),
            style = AppTypography.displaySmall
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

@Preview
@Composable
fun PreviewHome(){
    DCodeTheme {
        PreviewHome()
    }
}