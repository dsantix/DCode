package com.dsanti.dcode.ui.scan.components

import android.app.PendingIntent.getActivity
import android.content.ContentValues
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.PatternMatcher
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.dsanti.dcode.R
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.journeyapps.barcodescanner.BarcodeResult
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetResult(result: BarcodeResult) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var text by remember {
        mutableStateOf("")
    }

    text = result.text!!


    Column(
        Modifier
            .background(Color.White)
            .fillMaxWidth()) {

        with(result.text){
            when {
                this?.startsWith("tel:") == true -> {
                    Card(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)) {
                        FaIcon(faIcon = FaIcons.Phone, modifier = Modifier
                            .padding(16.dp), size = 38.dp)
                    }
                }

                this?.startsWith("smsto:") == true -> {
                    Card(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)) {
                        FaIcon(faIcon = FaIcons.Comment, modifier = Modifier
                            .padding(16.dp), size = 38.dp)
                    }
                }

                this?.startsWith("mailto:") == true -> {
                    Card(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)) {
                        FaIcon(faIcon = FaIcons.At, modifier = Modifier
                            .padding(16.dp), size = 38.dp)
                    }
                }

                this?.startsWith("WIFI:") == true -> {
                    Card(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)) {
                        FaIcon(faIcon = FaIcons.Wifi, modifier = Modifier
                            .padding(16.dp), size = 38.dp)
                    }
                }

                this?.startsWith("BEGIN:VCARD") == true -> {
                    Card(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)) {
                        FaIcon(faIcon = FaIcons.AddressCard, modifier = Modifier
                            .padding(16.dp), size = 38.dp)
                    }
                }

                this?.startsWith("BEGIN:VEVENT") == true -> {
                    Card(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)) {
                        FaIcon(faIcon = FaIcons.Calendar, modifier = Modifier
                            .padding(16.dp), size = 38.dp)
                    }
                }

                else -> {
                    Card(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)) {
                        FaIcon(faIcon = FaIcons.Bold, modifier = Modifier
                            .padding(16.dp), size = 38.dp)
                    }
                }
            }
        }

        Text(text = textContent(text), modifier = Modifier
            .padding(vertical = 26.dp, horizontal = 8.dp)
            .fillMaxWidth())


        with(result.text){
            when {
                this?.startsWith("tel:") == true -> {
                    Column(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            val intent = Intent(Intent.ACTION_DIAL, result.text.toUri())
                            context.startActivity(intent)
                        }) {
                        Icon(imageVector = Icons.Rounded.Call, contentDescription = null, modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 6.dp)
                            )
                        
                        Text(text = stringResource(id = R.string.action_makecall), modifier = Modifier.padding(bottom = 8.dp))
                        
                    }
                    
                }

                this?.startsWith("smsto:") == true -> {

                    val smsto = Regex("^([^:]*:[^:]*)").find(result.text)

                    val smsbody = Regex("(?<=smsto:).*").find(result.text)?.groups?.first()?.value?.substringAfter(":")


                    
                    Column(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            val intent = Intent(
                                Intent.ACTION_SENDTO,
                                smsto?.groups?.first()?.value
                                    ?.substringBeforeLast(":")
                                    ?.toUri() ?: "".toUri()
                            ).apply {
                                putExtra("sms_body", smsbody)
                            }
                            context.startActivity(intent)
                        }) {
                        Icon(imageVector = Icons.Rounded.Sms, contentDescription = null, modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 6.dp)
                            )
                        
                        Text(text = stringResource(id = R.string.action_sendsms), modifier = Modifier.padding(bottom = 8.dp))
                    }
                    
                }

                this?.startsWith("mailto:") == true -> {
                    Column(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            val mail = result.text.substringAfter("mailto:")
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_EMAIL, mail)
                            }
                            context.startActivity(
                                Intent.createChooser(
                                    intent,
                                    "Send email"
                                )
                            )
                        }) {
                        Icon(imageVector = Icons.Rounded.Email, contentDescription = null, modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 6.dp))

                        Text(text = stringResource(id = R.string.action_sendmail), modifier = Modifier.padding(bottom = 8.dp))
                    }

                }

                this?.startsWith("WIFI:") == true -> {
                    val ssid = Regex("(S:.*?;)").find(result.text)?.groups?.first()?.value?.removePrefix("S:")?.removeSuffix(";")
                    val password = Regex("(P:.*?;)").find(result.text)?.groups?.first()?.value?.removePrefix("P:")?.removeSuffix(";")
                    val isHidden = Regex("(H:.*?;)").find(result.text)?.groups?.first()?.value?.removePrefix("H:")?.removeSuffix(";").toBoolean()
                    Column(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                if (ssid != null) {
                                    connectToWifi(ssid, password, context, isHidden)
                                }
                            }) {
                        Icon(imageVector = Icons.Rounded.Wifi, contentDescription = null, modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 6.dp)
                            )
                        
                        Text(text = stringResource(id = R.string.action_connectwifi), textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 8.dp))
                    }
                }

                this?.startsWith("BEGIN:VCARD") == true -> {
                    val name = Regex("(N:.*)").find(result.text)?.groups?.first()?.value
                    val company = Regex("(ORG:.*)").find(result.text)?.groups?.first()?.value
                    val title = Regex("(TITLE:.*)").find(result.text)?.groups?.first()?.value
                    val phoneNumber = Regex("(TEL:.*)").find(result.text)?.groups?.first()?.value
                    val email = Regex("(EMAIL:.*)").find(result.text)?.groups?.first()?.value
                    val address = Regex("(ADR:.*)").find(result.text)?.groups?.first()?.value
                    val website = Regex("(URL:.*)").find(result.text)?.groups?.first()?.value
                    val note = Regex("(NOTE:.*)").find(result.text)?.groups?.first()?.value
                    Column(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            addContact(
                                name = name,
                                company = company,
                                title = title,
                                phoneNumber = phoneNumber,
                                email = email,
                                address,
                                website,
                                note,
                                context
                            )
                        }) {
                        Icon(imageVector = Icons.Rounded.ContactPhone, contentDescription = null, modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 6.dp)
                        )

                        Text(text = stringResource(id = R.string.action_addcontact), modifier = Modifier.padding(bottom = 8.dp))
                    }
                }

                this?.startsWith("MECARD:") == true -> {
                    val name = Regex("(N:.*?;)").find(result.text)?.groups?.first()?.value
                    val company = Regex("(ORG:.*?;)").find(result.text)?.groups?.first()?.value
                    val title = Regex("(TITLE:.*?;)").find(result.text)?.groups?.first()?.value
                    val phoneNumber = Regex("(TEL:.*?;)").find(result.text)?.groups?.first()?.value
                    val email = Regex("(EMAIL:.*?;)").find(result.text)?.groups?.first()?.value
                    val address = Regex("(ADR:.*?;)").find(result.text)?.groups?.first()?.value
                    val website = Regex("(URL:.*?;)").find(result.text)?.groups?.first()?.value
                    val note = Regex("(NOTE:.*?;)").find(result.text)?.groups?.first()?.value
                    Column(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            addContact(
                                name = name,
                                company = company,
                                title = title,
                                phoneNumber = phoneNumber,
                                email = email,
                                address,
                                website,
                                note,
                                context
                            )
                        }) {
                        Icon(imageVector = Icons.Rounded.ContactPhone, contentDescription = null, modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 6.dp)
                        )

                        Text(text = stringResource(id = R.string.action_addcontact), modifier = Modifier.padding(bottom = 8.dp))
                    }
                }

                this?.startsWith("BEGIN:VEVENT") == true -> {

                    val eventTitle = Regex("(SUMMARY:.*)").find(result.text)?.groups?.first()?.value
                    val eventLocation = Regex("(LOCATION:.*)").find(result.text)?.groups?.first()?.value
                    val eventDescription = Regex("(DESCRIPTION:.*)").find(result.text)?.groups?.first()?.value

                    val isEventAllDay = result.text.contains("DTSTART;VALUE=DATE")

                    val eventStartDate : Long
                    val eventEndDate : Long

                    if (result.text.contains("DTSTART;VALUE=DATE")){
                        eventStartDate = convertDateToMillis(Regex("(DTSTART;VALUE=DATE:.*)").find(result.text)?.groups?.first()?.value, isEventAllDay)
                        eventEndDate = convertDateToMillis(Regex("(DTEND;VALUE=DATE:.*)").find(result.text)?.groups?.first()?.value, isEventAllDay)
                    }else{
                        eventStartDate = convertDateToMillis(Regex("(DTSTART:.*)").find(result.text)?.groups?.first()?.value, isEventAllDay)
                        eventEndDate = convertDateToMillis(Regex("(DTEND:.*)").find(result.text)?.groups?.first()?.value, isEventAllDay)
                    }

                    Column(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            addCalendarEvent(isEventAllDay, eventTitle, eventStartDate, eventEndDate, eventLocation, eventDescription, context)
                        }) {
                        Icon(imageVector = Icons.Rounded.Event, contentDescription = null, modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 6.dp)
                        )

                        Text(text = stringResource(id = R.string.action_addevent), modifier = Modifier.padding(bottom = 8.dp))
                    }
                }
                else -> {
                    Row(Modifier.align(Alignment.CenterHorizontally)) {
                        Column(modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .clickable {
                                clipboardManager.setText(AnnotatedString(result.text))
                                Toast
                                    .makeText(context, R.string.copied_text, Toast.LENGTH_SHORT)
                                    .show()
                            }) {
                            Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = null,modifier = Modifier.align(Alignment.CenterHorizontally))
                            Text(text = stringResource(id = R.string.action_copytext), modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        Column(modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .clickable {
                                TODO()
                            }) {
                            FaIcon(faIcon = FaIcons.AlignCenter, modifier = Modifier
                                .rotate(90f)
                                .align(Alignment.CenterHorizontally))
                            Text(text = stringResource(id = R.string.action_readtext), modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        Column(modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .clickable {
                                TODO()
                            }) {
                            Icon(imageVector = Icons.Rounded.Bookmark, contentDescription = null,modifier = Modifier.align(Alignment.CenterHorizontally))
                            Text(text = stringResource(id = R.string.action_save), modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        Column(modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .clickable {
                                TODO()
                            }) {
                            FaIcon(faIcon = FaIcons.ArrowAltCircleUpRegular, modifier = Modifier.align(Alignment.CenterHorizontally))
                            Text(text = stringResource(id = R.string.action_share), modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                }
            }
        }

    }
}

@Stable
@Composable
private fun textContent(text: String) : String {

    return with(text){
        when{
            //Tel
            startsWith("tel:") -> {
                text.removePrefix("tel:")
            }
            //SMS
            startsWith("smsto:") -> {
                text.removePrefix("smsto:")
            }
            //Email
            startsWith("mailto:") -> {
                text.removePrefix("mailto:")
            }
            //Wifi
            startsWith("WIFI:") -> {
                text.replace(regex = Regex("(P:.*?;)"), replacement = "P:********;")
            }
            //vCard
            startsWith("BEGIN:VCARD") -> {
                text.substringAfter("VERSION:3.0")
                    .substringBefore("END:VCARD")
                    .replace("N:", stringResource(id = R.string.vcard_name))
                    .replace("ORG:", stringResource(id = R.string.vcard_org))
                    .replace("TITLE:", stringResource(id = R.string.vcard_title))
                    .replace("TEL:", stringResource(id = R.string.vcard_tel))
                    .replace("URL:", stringResource(id = R.string.vcard_url))
                    .replace("EMAIL:", stringResource(id = R.string.vcard_email))
                    .replace("ADR:", stringResource(id = R.string.vcard_address))
                    .replace("NOTE:", stringResource(id = R.string.vcard_note))
            }
            //vCard
            startsWith("MECARD:") -> {
                text.replace("N:", stringResource(id = R.string.vcard_name))
                    .replace("ORG:", stringResource(id = R.string.vcard_org))
                    .replace("TITLE:", stringResource(id = R.string.vcard_title))
                    .replace("TEL:", stringResource(id = R.string.vcard_tel))
                    .replace("URL:", stringResource(id = R.string.vcard_url))
                    .replace("EMAIL:", stringResource(id = R.string.vcard_email))
                    .replace("ADR:", stringResource(id = R.string.vcard_address))
                    .replace("NOTE:", stringResource(id = R.string.vcard_note))
            }
            //Event
            startsWith("BEGIN:VEVENT") -> {
                text.substringAfter("BEGIN:VEVENT")
                    .substringBefore("END:VEVENT")
                    .replace("SUMMARY:", stringResource(id = R.string.vevent_title))
                    .replace("DTSTART:", stringResource(id = R.string.vevent_startDate))
                    .replace("DTEND:", stringResource(id = R.string.vevent_endDate))
                    .replace("DTSTART;VALUE=DATE:", stringResource(id = R.string.vevent_startDate))
                    .replace("DTEND;VALUE=DATE:", stringResource(id = R.string.vevent_endDate))
                    .replace("LOCATION:", stringResource(id = R.string.vevent_location))
                    .replace("DESCRIPTION:", stringResource(id = R.string.vevent_description))
            }
            else -> {
                text
            }
        }
    }
}

@Suppress("DEPRECATION")
private fun connectToWifi(ssid:String, password:String?, context:Context, isHidden:Boolean){

    val specifier: WifiNetworkSpecifier?

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){


        specifier = if (password != null && isHidden){
            WifiNetworkSpecifier.Builder()
                .setSsidPattern(PatternMatcher(ssid, PatternMatcher.PATTERN_PREFIX))
                .setWpa2Passphrase(password)
                .setIsHiddenSsid(true)
                .build()
        }else if (password != null && !isHidden){
            WifiNetworkSpecifier.Builder()
                .setSsidPattern(PatternMatcher(ssid, PatternMatcher.PATTERN_PREFIX))
                .setWpa2Passphrase(password)
                .setIsHiddenSsid(false)
                .build()
        }else{
            WifiNetworkSpecifier.Builder()
                .setSsidPattern(PatternMatcher(ssid, PatternMatcher.PATTERN_PREFIX))
                .build()
        }


        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .setNetworkSpecifier(specifier)
            .build()

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                connectivityManager.bindProcessToNetwork(network)
            }

        }
        connectivityManager.requestNetwork(request, networkCallback)


        //connectivityManager.unregisterNetworkCallback(networkCallback)
    }else{
        val wifiConfig = WifiConfiguration()
        wifiConfig.SSID = java.lang.String.format("\"%s\"", ssid)
        wifiConfig.preSharedKey = java.lang.String.format("\"%s\"", password)
        val wifiManager = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        if (!wifiManager.isWifiEnabled) wifiManager.isWifiEnabled = true
        val netId: Int = wifiManager.addNetwork(wifiConfig)
        wifiManager.disconnect()
        wifiManager.enableNetwork(netId, true)
        wifiManager.reconnect()
    }

}

private fun addContact(name:String?, company: String?, title:String?, phoneNumber:String?, email:String?, address:String?, website:String?, note:String?, context: Context){
    Intent(ContactsContract.Intents.Insert.ACTION).apply {
        type = ContactsContract.RawContacts.CONTENT_TYPE

        if (name != null){
            putExtra(ContactsContract.Intents.Insert.NAME, name)
        }

        if (company != null){
            putExtra(ContactsContract.Intents.Insert.COMPANY, company)
        }

        if (title != null){
            putExtra(ContactsContract.Intents.Insert.JOB_TITLE, title)
        }

        if (phoneNumber != null){
            putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber)
        }

        if (email != null){
            putExtra(ContactsContract.Intents.Insert.EMAIL, email)
        }

        if (note != null){
            putExtra(ContactsContract.Intents.Insert.NOTES, note)
        }

        if (address != null){
            putExtra(ContactsContract.Intents.Insert.POSTAL, address)
        }

        if (website != null){
            val arrayRow = arrayListOf<ContentValues>()

            val row = ContentValues()
            row.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
            row.put(ContactsContract.CommonDataKinds.Website.TYPE, ContactsContract.CommonDataKinds.Website.TYPE_PROFILE)
            row.put(ContactsContract.CommonDataKinds.Website.URL, website)
            arrayRow.add(row)

            putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, arrayRow)
        }


        context.startActivity(this)
    }
}

private fun addCalendarEvent(isAllDayEvent: Boolean, eventTitle:String?, eventStartDate:Long?, eventEndDate:Long?, eventLocation:String?, eventDescription:String?, context: Context){

    Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI

        if (eventTitle != null ){
            putExtra(CalendarContract.Events.TITLE, eventTitle)
        }

        if (eventStartDate != null ){
            putExtra(CalendarContract.Events.DTSTART, eventStartDate)
        }

        if (eventEndDate != null ){
            putExtra(CalendarContract.Events.DTEND, eventEndDate)
        }

        if (eventLocation != null ){
            putExtra(CalendarContract.Events.EVENT_LOCATION, eventLocation)
        }

        if (eventDescription != null ){
            putExtra(CalendarContract.Events.DESCRIPTION, eventDescription)
        }

        putExtra(CalendarContract.Events.ALL_DAY, isAllDayEvent)

        context.startActivity(this)
    }

}

private fun convertDateToMillis(text: String?, isAllDayEvent: Boolean) : Long{

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val date = Instant.parse(text)
        return date.toEpochMilli()
    }else{

        val dateFormart = if (isAllDayEvent) SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) else SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())

        val date = dateFormart.parse(text)


        if (date != null) {
            return date.time
        }
    }
    return 0L
}