import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dsanti.dcode.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import java.util.Calendar
import java.util.Date

/**
 * A Jetpack Compose compatible Date Picker.
 * @author Arnau Mora, Joao Gavazzi, Rafael DSanti
 * @param minDate The minimum date allowed to be picked.
 * @param maxDate The maximum date allowed to be picked.
 * @param onDateSelected Will get called when a date gets picked.
 * @param onDismissRequest Will get called when the user requests to close the dialog.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun DateAndTimePicker(
    minDate: Long? = null,
    maxDate: Long? = null,
    showTime:Boolean,
    onDateSelected: (Date) -> Unit,
    onDismissRequest: () -> Unit
) {
    val selDate = remember { mutableStateOf(Calendar.getInstance().time) }


    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {

                Text(
                    text = stringResource(id = R.string.select_date),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    text = DateFormat.format("MMM d, yyyy", selDate.value).toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            CustomCalendarView(
                minDate,
                maxDate,
                onDateSelected = {
                    selDate.value = it
                }
            )

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                Button(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.textButtonColors(),
                ) {

                    Text(
                        text = stringResource(id = R.string.action_cancel),
                    )
                }

                Button(
                    onClick = {
                        val newDate = selDate.value
                        onDateSelected(
                            // This makes sure date is not out of range
                            Date(
                                maxOf(
                                    minOf(maxDate ?: Long.MAX_VALUE, newDate.time),
                                    minDate ?: Long.MIN_VALUE
                                )
                            )
                        )
                        onDismissRequest()
                    },
                    colors = ButtonDefaults.textButtonColors(),
                ) {

                    Text(
                        text = stringResource(id = R.string.ok),
                    )
                }

            }
        }
    }
}

/**
 * Used at [DatePicker] to create the calendar picker.
 * @author Arnau Mora, Joao Gavazzi
 * @param minDate The minimum date allowed to be picked.
 * @param maxDate The maximum date allowed to be picked.
 * @param onDateSelected Will get called when a date is selected.
 */
@Composable
private fun CustomCalendarView(
    minDate: Long? = null,
    maxDate: Long? = null,
    onDateSelected: (Date) -> Unit
) {
    // Adds view to Compose
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(ContextThemeWrapper(context, R.style.CalenderViewCustom))
        },
        update = { view ->
            if (minDate != null)
                view.minDate = minDate
            if (maxDate != null)
                view.maxDate = maxDate

            view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                onDateSelected(
                    Calendar
                        .getInstance()
                        .apply {
                            set(year, month, dayOfMonth)
                        }
                        .time
                )
            }
        }
    )
}

@Composable
fun TimePicker(context: Context, currentHour:Int, currentMinute:Int, onTimeSelected: (String) -> Unit, onDismissRequest: (Boolean) -> Unit) {

    val timePicker = TimePickerDialog(
        context,
        {_, hour : Int, minute: Int ->
            onTimeSelected("$hour:$minute")
        }, currentMinute, currentHour, true
    )
    timePicker.show()

    onDismissRequest(timePicker.isShowing)

}

@Preview
@Composable
fun PreviewDateAndPicker() {
    DateAndTimePicker(onDateSelected = {

    }, onDismissRequest = {

    }, showTime = true)
}