package com.example.julen.nativeappsproject

import android.arch.persistence.room.TypeConverter
import org.joda.time.DateTime


class Converter {

    @TypeConverter
    fun fromTimestamp(value: String?): DateTime? {
        return value?.let { DateTime(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: DateTime?): String? {
        return date.toString()
    }
}