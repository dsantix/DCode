package com.dsanti.dcode.data

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun datestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }
}