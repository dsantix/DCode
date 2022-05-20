package com.dsanti.dcode.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dsanti.dcode.R
import com.dsanti.dcode.Screen
import com.dsanti.dcode.ui.header
import com.dsanti.dcode.ui.theme.AppTypography
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


@Composable
fun GridListContentScrolling() {

    val tint = MaterialTheme.colorScheme.onSurface

    val listItems = listOf(QrGeneralItems.Url, QrGeneralItems.Email, QrGeneralItems.Vcard, QrGeneralItems.Number, QrGeneralItems.SmsFacetime,
        QrGeneralItems.CalendarEvent, QrGeneralItems.Wifi, QrGeneralItems.AppStore)

    val socialItems = listOf(QrGeneralItems.Instagram, QrGeneralItems.Spotify, QrGeneralItems.TikTok,
        QrGeneralItems.Facebook, QrGeneralItems.Pinterest, QrGeneralItems.Whatsapp, QrGeneralItems.Discord)

    val state = rememberLazyGridState()


    LazyVerticalGrid(columns = GridCells.Fixed(2), state = state,horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp),content = {
        
        items(listItems.size) { index ->
            Card(backgroundColor = MaterialTheme.colorScheme.surfaceVariant,elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {

                    }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                    Icon(imageVector = listItems[index].icon!!, contentDescription = "", tint = tint)
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = stringResource(id = listItems[index].title), color = tint)
                }
            }
        }

        header {
            Text(text = stringResource(id =  R.string.tab_social), style = AppTypography.displaySmall, modifier = Modifier.padding(start = 8.dp))
        }

        items(socialItems.size){ index ->
            Card(backgroundColor = MaterialTheme.colorScheme.surfaceVariant , elevation = 4.dp,modifier = Modifier
                .clickable {

                }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                    FaIcon(faIcon = socialItems[index].faIcon!!, modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 18.dp),
                    tint = tint)

                    Text(text = stringResource(id = socialItems[index].title), modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp),
                    color = tint)

                    Card(modifier = Modifier
                        .padding(horizontal = 32.dp, vertical = 14.dp)
                        .align(Alignment.CenterHorizontally), backgroundColor = socialItems[index].colorCard!!) {

                        Column {
                            Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)){
                                Image(painter = painterResource(id = R.drawable.custom_qr_code), contentDescription = null)
                                Image(painter = painterResource(id = socialItems[index].iconBrandColor!!), contentDescription = null, modifier = Modifier.align(Alignment.Center))
                            }

                            Text(text = "Follow us", modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 8.dp), color = Color.White)
                        }
                    }
                }
            }
        }

    })
}


@Composable
fun SocialItems() {


}

sealed class QrGeneralItems(val icon: ImageVector?, val faIcon: FaIconType?, @StringRes val title:Int, val screen: Screen?, val colorCard: Color?, val iconBrandColor: Int?){
    object Url : QrGeneralItems(Icons.Rounded.Link, null,R.string.qr_url, null, colorCard = null , null)
    object Email : QrGeneralItems(Icons.Rounded.Email, null,R.string.qr_email, null, colorCard = null, null)
    object Vcard : QrGeneralItems(Icons.Rounded.CardMembership, null,R.string.qr_vcard, null, colorCard = null, null)
    object Number : QrGeneralItems(Icons.Rounded.CellTower, null,R.string.qr_number, null, colorCard = null, null)
    object SmsFacetime : QrGeneralItems(Icons.Rounded.Sms, null,R.string.qr_sms_facetime, null, colorCard = null, null)
    object CalendarEvent : QrGeneralItems(Icons.Rounded.Event, null,R.string.qr_calendar_event, null, colorCard =  null, null)
    object Wifi : QrGeneralItems(Icons.Rounded.Wifi, null,R.string.qr_wifi, null, colorCard = null, null)
    object AppStore : QrGeneralItems(Icons.Rounded.Store, null,R.string.qr_app_store, null, colorCard = null, null)
    object Instagram : QrGeneralItems(null, FaIcons.Instagram, R.string.qr_instagram, null, colorCard = Color(0xFF515BD4), R.drawable.ic_instagram_color)
    object Spotify : QrGeneralItems(null, FaIcons.Spotify, R.string.qr_spotify, null, colorCard = Color(0xFF1DB954), R.drawable.ic_spotify_color)
    object TikTok : QrGeneralItems(null, FaIcons.Tiktok, R.string.qr_tiktok, null, colorCard = Color(0xFF000000), R.drawable.ic_tiktok_color)
    object Facebook : QrGeneralItems(null, FaIcons.Facebook, R.string.qr_facebook, null, colorCard = Color(0xFF4267B2), R.drawable.ic_facebook_color)
    object Pinterest : QrGeneralItems(null, FaIcons.Pinterest, R.string.qr_pinterest, null, colorCard = Color(0xFFE60023), R.drawable.ic_pinterest_color)
    object Whatsapp : QrGeneralItems(null, FaIcons.Whatsapp, R.string.qr_whatsapp, null, colorCard = Color(0xFF25D366), R.drawable.ic__whatsapp_color)
    object Discord : QrGeneralItems(null, FaIcons.Discord, R.string.qr_discord, null, colorCard = Color(0xFF5865F2), R.drawable.ic_discord_color)
}




