package com.dsanti.dcode.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material.icons.rounded.VpnKey
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dsanti.dcode.BuildConfig
import com.dsanti.dcode.R
import com.dsanti.dcode.Screen
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer


@Composable
fun About(modifier: Modifier, navController: NavController) {
    val versionName = BuildConfig.VERSION_NAME
    val buildType = BuildConfig.BUILD_TYPE
    val localUri = LocalUriHandler.current
    Box(modifier = modifier){
        Column {
            Row {
                Icon(imageVector = Icons.Rounded.Update, contentDescription = null, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp))
                Column {
                    Text(text = stringResource(id = R.string.version), modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp))
                    Text(text = "$buildType - $versionName", modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp))
                }
            }

            Row(Modifier.clickable {
                navController.navigate(Screen.Licenses.route)
            }) {
                Icon(imageVector = Icons.Rounded.VpnKey, contentDescription = null, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp))

                Text(text = stringResource(id = R.string.about_licenses), modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp))
            }

            Row(Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp)) {
                FaIcon(faIcon = FaIcons.Github, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp)
                    .clickable {
                        localUri.openUri("https://github.com/dsantix")
                    })

                FaIcon(faIcon = FaIcons.Twitter, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp)
                    .clickable {
                        localUri.openUri("https://twitter.com/dsanti___")
                    })

                FaIcon(faIcon = FaIcons.Twitch, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp)
                    .clickable {
                        localUri.openUri("https://www.twitch.tv/dsantiii")
                    })

                FaIcon(faIcon = FaIcons.Discord, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable {
                        localUri.openUri("https://discordapp.com/users/412250723225567252")
                    })

                Icon(painter = painterResource(id = R.drawable.ic_ko_fi_logo_rgb_dark), contentDescription = null, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp)
                    .clickable {
                        localUri.openUri("https://ko-fi.com/dsanti")
                    })
            }
        }
    }
}