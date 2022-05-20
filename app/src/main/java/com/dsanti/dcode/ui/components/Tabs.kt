package com.dsanti.dcode.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dsanti.dcode.ui.scan.ScanItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabRounded(tabs: List<ScanItems>, pagerState: PagerState, scope: CoroutineScope) {

    TabRow(selectedTabIndex = pagerState.currentPage,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50))
            .padding(top = 1.dp, start = 1.dp, end = 1.dp),
        indicator = {
            Box{}
        }
    ) {
        tabs.forEachIndexed { index, tab ->
            val selected = pagerState.currentPage == index
            Tab(
                modifier = if (selected) Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        MaterialTheme.colorScheme.onSurface
                    )
                else Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        MaterialTheme.colorScheme.surface
                    ),
                selected = selected,
                onClick = {scope.launch {
                    pagerState.animateScrollToPage(index)
                }},
                text = { Text(text = stringResource(tab.title), color = if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface) }
            )
        }
    }
}