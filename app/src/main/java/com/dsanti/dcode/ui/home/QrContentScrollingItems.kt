package com.dsanti.dcode.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CardMembership
import androidx.compose.material.icons.rounded.CellTower
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Sms
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dsanti.dcode.R
import com.dsanti.dcode.Screen
import com.dsanti.dcode.ui.components.AdBannerView
import com.dsanti.dcode.ui.theme.AppTypography
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons


@Composable
fun GridListContentScrolling(navController: NavController, paddingValues: PaddingValues) {

    val tint = MaterialTheme.colorScheme.onSurface

    val listItems = listOf(QrGeneralItems.Url, QrGeneralItems.Email, QrGeneralItems.Vcard, QrGeneralItems.Number, QrGeneralItems.SmsFacetime,
        QrGeneralItems.CalendarEvent, QrGeneralItems.Wifi, QrGeneralItems.AppStore)

    val socialItems = listOf(QrGeneralItems.Instagram, QrGeneralItems.Spotify, QrGeneralItems.TikTok,
        QrGeneralItems.Facebook, QrGeneralItems.Pinterest, QrGeneralItems.Whatsapp, QrGeneralItems.Discord)

    val state = rememberLazyGridState()

    Box(Modifier.fillMaxHeight().padding(paddingValues)) {

        Column {

            Icon(painter = painterResource(id = R.drawable.ic_qr_code_icon), contentDescription = stringResource(
                id = R.string.contentd_dcode_icon
            ), modifier = Modifier
                .align(CenterHorizontally)
                .size(44.dp))
            Text(text = stringResource(id = R.string.app_name), modifier = Modifier
                .padding(top = 1.dp)
                .align(CenterHorizontally))

            Text(text = stringResource(id = R.string.home_create),
                style = AppTypography.displaySmall
            )

            LazyVerticalGrid(columns = GridCells.Fixed(2), state = state,horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth(),content = {

                items(listItems.size) { index ->
                    Card(backgroundColor = MaterialTheme.colorScheme.surfaceVariant,elevation = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                listItems[index].screen?.route?.let { navController.navigate(it) }
                            }
                    ) {
                        Column(horizontalAlignment = CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                            Icon(imageVector = listItems[index].icon!!, contentDescription = "", tint = tint)
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(text = stringResource(id = listItems[index].title), color = tint, textAlign = TextAlign.Center, modifier = Modifier.align(CenterHorizontally))
                        }
                    }
                }

                /*

                header {
                    Text(text = stringResource(id =  R.string.tab_social), style = AppTypography.displaySmall, modifier = Modifier.padding(start = 8.dp))
                }

                items(socialItems.size){ index ->
                    Card(backgroundColor = MaterialTheme.colorScheme.surfaceVariant , elevation = 4.dp,modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable {
                            socialItems[index].screen?.route?.let { navController.navigate(it) }
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

                                    Text(text = stringResource(id = socialItems[index].bottomText!!), modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(bottom = 8.dp), color = Color.White, textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }
                }*/

            })

        }

        AdBannerView( isTest = false, modifier = Modifier.align(BottomCenter))
    }

}

sealed class QrGeneralItems(val icon: ImageVector?, val faIcon: FaIconType?, @StringRes val title:Int, val screen: Screen?, val colorCard: Color?, val iconBrandColor: Int?, @StringRes val bottomText : Int?){
    object Url : QrGeneralItems(Icons.Rounded.Link, null,R.string.qr_url, Screen.CreateUrl, colorCard = null , null, null)
    object Email : QrGeneralItems(Icons.Rounded.Email, null,R.string.qr_email, Screen.CreateEmail, colorCard = null, null, null)
    object Vcard : QrGeneralItems(Icons.Rounded.CardMembership, null,R.string.qr_vcard, Screen.CreateVCard, colorCard = null, null, null)
    object Number : QrGeneralItems(Icons.Rounded.CellTower, null,R.string.qr_number, Screen.CreatePhoneNumber, colorCard = null, null, null)
    object SmsFacetime : QrGeneralItems(Icons.Rounded.Sms, null,R.string.qr_sms_facetime, Screen.CreateSMS, colorCard = null, null, null)
    object CalendarEvent : QrGeneralItems(Icons.Rounded.Event, null,R.string.qr_calendar_event, Screen.CreateCalendarEvent, colorCard =  null, null, null)
    object Wifi : QrGeneralItems(Icons.Rounded.Wifi, null,R.string.qr_wifi, Screen.CreateWIFI, colorCard = null, null, null)
    object AppStore : QrGeneralItems(Icons.Rounded.Store, null,R.string.qr_app_store, Screen.CreateAppStore, colorCard = null, null, null)
    object Instagram : QrGeneralItems(null, FaIcons.Instagram, R.string.qr_instagram, null, colorCard = Color(0xFF515BD4), R.drawable.ic_instagram_color, R.string.followUs)
    object Spotify : QrGeneralItems(null, FaIcons.Spotify, R.string.qr_spotify, null, colorCard = Color(0xFF1DB954), R.drawable.ic_spotify_color, R.string.playlist)
    object TikTok : QrGeneralItems(null, FaIcons.Tiktok, R.string.qr_tiktok, null, colorCard = Color(0xFF000000), R.drawable.ic_tiktok_color, R.string.followUs)
    object Facebook : QrGeneralItems(null, FaIcons.Facebook, R.string.qr_facebook, null, colorCard = Color(0xFF4267B2), R.drawable.ic_facebook_color, R.string.followUs)
    object Pinterest : QrGeneralItems(null, FaIcons.Pinterest, R.string.qr_pinterest, null, colorCard = Color(0xFFE60023), R.drawable.ic_pinterest_color, R.string.followUs)
    object Whatsapp : QrGeneralItems(null, FaIcons.Whatsapp, R.string.qr_whatsapp, null, colorCard = Color(0xFF25D366), R.drawable.ic__whatsapp_color, R.string.sendMessage)
    object Discord : QrGeneralItems(null, FaIcons.Discord, R.string.qr_discord, null, colorCard = Color(0xFF5865F2), R.drawable.ic_discord_color, R.string.discordServer)
}




