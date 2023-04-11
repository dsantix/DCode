package com.dsanti.dcode.ui.scan.components

import android.content.ContentValues
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.dsanti.dcode.R
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.journeyapps.barcodescanner.BarcodeResult
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BottomSheetResult(result: BarcodeResult?, uploadText: String?) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val text = result?.text?.let { textContent(text = it) }


    val textWithoutChange = result?.text

    val vcard = VCard()

    val lines = textWithoutChange?.split("\n")

    Column(
        Modifier
            .background(Color.White)
            .fillMaxWidth()) {


        //Icons
        with(textWithoutChange){
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

        //Result Text
        if (text != null) {
            Text(text = text, modifier = Modifier
                .padding(vertical = 26.dp, horizontal = 8.dp)
                .fillMaxWidth())
        }

        //Icons with click
        with(textWithoutChange){
            when {
                //Telefone
                this?.startsWith("tel:") == true -> {
                    Column(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            val intent = Intent(Intent.ACTION_DIAL, result?.text?.toUri())
                            context.startActivity(intent)
                        }) {
                        Icon(imageVector = Icons.Rounded.Call, contentDescription = null, modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 6.dp)
                            )
                        
                        Text(text = stringResource(id = R.string.action_makecall), modifier = Modifier.padding(bottom = 8.dp))
                    }
                    
                }

                //SmS
                this?.startsWith("smsto:") == true -> {

                    val smsto = Regex("^([^:]*:[^:]*)").find(textWithoutChange!!)

                    val smsbody = Regex("(?<=smsto:).*").find(textWithoutChange)?.groups?.first()?.value?.substringAfter(":")


                    
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

                //Email
                this?.startsWith("mailto:") == true -> {
                    Column(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            val mail = textWithoutChange!!.substringAfter("mailto:")
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

                //Wifi
                this?.startsWith("WIFI:") == true -> {

                    val ssid = textWithoutChange?.substringAfter("S:")?.substringBefore(";")
                    val password = textWithoutChange?.substringAfter("P:")?.substringBefore(";;")



                    Column(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                if (ssid != null) {
                                    if (password != null) {
                                        connectToWifi(ssid, password, context)
                                    }
                                }
                            }) {
                        Icon(imageVector = Icons.Rounded.Wifi, contentDescription = null, modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 6.dp)
                            )
                        
                        Text(text = stringResource(id = R.string.action_connectwifi), textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 8.dp))
                    }
                }

                //VCARD E MECARD
                this?.startsWith("BEGIN:VCARD") == true -> {

                    lines?.forEach { line ->
                        when {
                            line.startsWith("N:") -> vcard.name = line.substring(2)
                            line.startsWith("ORG:") -> vcard.company = line.substring(4)
                            line.startsWith("TITLE:") -> vcard.title = line.substring(6)
                            line.startsWith("TEL:") -> vcard.phoneNumber = line.substring(line.indexOf(":") + 1)
                            line.startsWith("EMAIL:") -> vcard.email = line.substring(line.indexOf(":") + 1)
                            line.startsWith("ADR:") -> vcard.address = line.substring(line.indexOf(":") + 1)
                            line.startsWith("URL:") -> vcard.website = line.substring(4)
                            line.startsWith("NOTE:") -> vcard.note = line.substring(5)
                        }
                    }

                    Column(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            addContact(
                                name = vcard.name,
                                company = vcard.company,
                                title = vcard.title,
                                phoneNumber = vcard.phoneNumber,
                                email = vcard.email,
                                address = vcard.address,
                                website = vcard.website,
                                note = vcard.note,
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

                    Column(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            addContact(
                                name = vcard.name,
                                company = vcard.company,
                                title = vcard.title,
                                phoneNumber = vcard.phoneNumber,
                                email = vcard.email,
                                address = vcard.address,
                                website = vcard.website,
                                note = vcard.note,
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

                //Evento
                this?.startsWith("BEGIN:VEVENT") == true -> {

                    val eventTitle = Regex("(SUMMARY:(.*)\\n)").find(textWithoutChange!!)?.groups?.first()?.value
                    val eventLocation = Regex("(LOCATION:(.*)\\n)").find(textWithoutChange)?.groups?.first()?.value
                    val eventDescription = Regex("(DESCRIPTION:(.*)\\n)").find(textWithoutChange)?.groups?.first()?.value

                    val isEventAllDay = textWithoutChange.contains("DTSTART;VALUE=DATE")

                    val eventStartDate : Long
                    val eventEndDate : Long

                    if (textWithoutChange.contains("DTSTART;VALUE=DATE")) {
                        eventStartDate = Regex("(DTSTART;VALUE=DATE:.*)").find(textWithoutChange)?.groups?.first()?.value?.toLong()!!
                        eventEndDate = Regex("(DTEND;VALUE=DATE:.*)").find(textWithoutChange)?.groups?.first()?.value?.toLong()!!
                    }else{
                        eventStartDate = Regex("(DTSTART:.*)").find(textWithoutChange)?.groups?.first()?.value?.toLong()!!
                        eventEndDate = Regex("(DTEND:.*)").find(textWithoutChange)?.groups?.first()?.value?.toLong()!!
                    }

                    Column(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            /*addCalendarEvent(
                                isEventAllDay,
                                eventTitle,
                                eventStartDate,
                                eventEndDate,
                                eventLocation,
                                eventDescription,
                                context
                            )*/
                        }) {
                        Icon(imageVector = Icons.Rounded.Event, contentDescription = null, modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 6.dp)
                        )

                        Text(text = stringResource(id = R.string.action_addevent), modifier = Modifier.padding(bottom = 8.dp))
                    }
                }
                // Outros
                else -> {
                    Row(Modifier.align(Alignment.CenterHorizontally)) {
                        Column(modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .clickable {
                                text?.let { AnnotatedString(it) }
                                    ?.let { clipboardManager.setText(it) }
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
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    putExtra(Intent.EXTRA_TEXT, text)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(shareIntent, null))
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
fun textContent(text: String) : String {

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
                text.replace(regex = Regex("(P:.*?;)"), replacement = "P:;")
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




private fun connectToWifi(ssid: String, password:String, applicationContext: Context) {
    val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
        try {
            val wifiConfig = WifiConfiguration()
            wifiConfig.SSID = "\"" + ssid + "\""
            wifiConfig.preSharedKey = "\"" + password + "\""
            val netId = wifiManager!!.addNetwork(wifiConfig)
            wifiManager.disconnect()
            wifiManager.enableNetwork(netId, true)
            wifiManager.reconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else {
        // Cria a especificação da rede Wi-Fi.
        val specifier = WifiNetworkSpecifier.Builder()
            .setSsid(ssid)
            .setWpa2Passphrase(password)
            .build()

        // Cria a configuração da rede Wi-Fi.
        val networkConfig = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .setNetworkSpecifier(specifier)
            .build()

        // Obtém o objeto ConnectivityManager.
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Cria um callback para monitorar as alterações na conectividade.
        val networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                // Conecta-se à nova rede.
                connectivityManager.bindProcessToNetwork(network)
                // Remove o callback.
                connectivityManager.unregisterNetworkCallback(this)
            }
        }

        connectivityManager.requestNetwork(networkConfig, networkCallback, 10000)
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

data class VCard(
    var name: String? = null,
    var company: String? = null,
    var title: String? = null,
    var phoneNumber: String? = null,
    var email: String? = null,
    var address: String? = null,
    var website: String? = null,
    var note: String? = null
)