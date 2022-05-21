package com.dsanti.dcode.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dsanti.dcode.data.AppDatabase
import com.dsanti.dcode.data.QRCode
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.dsanti.dcode.R
import com.dsanti.dcode.ui.scan.components.textContent

@Composable
fun History(modifier: Modifier) {

    val context = LocalContext.current

    val db = AppDatabase.getInstance(context)

    val qrCodesList = db?.qrCodeDao()?.getAll()

    qrCodesList?.let {
        HistoryList(qrCodes = it, modifier = modifier)
    }

}

@Composable
fun HistoryList(qrCodes: List<QRCode>, modifier: Modifier) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }


    Box {

        LazyColumn(state = listState, modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items = qrCodes, key = { it.qid }) { qrCode ->
                HistoryItem(qrCode = qrCode)
            }
        }


        AnimatedVisibility(visible = showButton, modifier = Modifier.align(Alignment.BottomCenter)) {
            ScrollToTopButton(onClick = {
                scope.launch {
                    listState.animateScrollToItem(index = 0)
                }
            })
        }
    }
}

@Composable
fun ScrollToTopButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = stringResource(id = R.string.scrollToTop))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryItem(qrCode: QRCode) {
    val calendar = Calendar.getInstance().apply { timeInMillis = qrCode.qrCodeDate }.time

    var date by remember {
        mutableStateOf("")
    }

    var text by remember {
        mutableStateOf("")
    }

    date = SimpleDateFormat.getDateTimeInstance().format(calendar)

    text = textContent(text = qrCode.qrCodeText)

    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)) {
        Column {
            Text(text = date, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))

            Text(text = text, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
    }

}