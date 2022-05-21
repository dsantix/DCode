package com.dsanti.dcode.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dsanti.dcode.data.Update
import com.dsanti.dcode.ui.theme.AppTypography
import com.dsanti.dcode.ui.theme.DCodeTheme
import kotlinx.coroutines.launch

val updates = listOf(Update(version = "0.10", date = "May 20, 2022", version_label = "Beta", whatsNew = listOf("Nothing"), fixed = listOf("Nothing"), improved = listOf("Nothing")),
    Update(version = "0.20", date = "May 20, 2022", version_label = "Beta", whatsNew = listOf("Nothing"), fixed = listOf("Nothing"), improved = listOf("Nothing")))

@Composable
fun Changelog(modifier: Modifier) {

    ChangelogList(updates = updates, modifier = modifier)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangelogList(updates: List<Update>, modifier: Modifier) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    Box {

        LazyColumn(state = listState, modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(updates){ update ->
                ChangelogItem(update = update)
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

@ExperimentalMaterial3Api
@Composable
fun ChangelogItem(update: Update) {

    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)) {
        Column {
            //Date
            Text(text = update.date, Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.ExtraBold)
            Row(Modifier.padding(horizontal = 8.dp, vertical = 2.dp)) {
                //Version
                Text(text = "v${update.version}", Modifier.align(Alignment.CenterVertically), fontWeight = FontWeight.ExtraBold)
                Card(Modifier.padding(horizontal = 6.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Text(text = update.version_label, Modifier.padding(4.dp))
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

    }
}