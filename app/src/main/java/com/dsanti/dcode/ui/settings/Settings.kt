package com.dsanti.dcode.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dsanti.dcode.R
import com.dsanti.dcode.Screen
import com.dsanti.dcode.ui.theme.AppTypography
import com.dsanti.dcode.ui.theme.Quicksand

@Composable
fun Settings(listSettings : List<SettingsItem>, navController: NavController) {

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 16.dp, horizontal = 8.dp)){
        Column {
            Icon(painter = painterResource(id = R.drawable.ic_qr_code_icon), contentDescription = null, modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally))
            Text(text = stringResource(id = R.string.app_name), modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(30.dp))
            LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(listSettings){ item ->
                    ItemRowSettings(icon = item.Icon, text = item.Title, activity = item.Activity, navController = navController)
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(text = stringResource(id = R.string.from), Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                Modifier
                    .align(Alignment.CenterHorizontally)) {
                Icon(painter = painterResource(id = R.drawable.ic_dsanti_icon_nonstop), contentDescription = null, modifier = Modifier.padding(horizontal = 8.dp))
                Text(text = stringResource(id = R.string.dsanti), style = AppTypography.bodySmall, modifier = Modifier.align(Alignment.CenterVertically))
            }

        }

    }
}

@Composable
fun ItemRowSettings(icon: ImageVector, @StringRes text: Int, activity: Screen, navController: NavController) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            navController.navigate(activity.route)
        }) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.align(Alignment.CenterVertically))
        Text(text = stringResource(id = text), modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterVertically)
            .padding(start = 24.dp, top = 16.dp, bottom = 16.dp),
        style = AppTypography.bodyLarge)
    }
}


data class SettingsItem(val Icon: ImageVector, val Title: Int, val Activity: Screen)