package com.example.smartcampuscompanion.data.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import androidx.room.TypeConverter

class TypeConverters {
    @TypeConverter
    fun fromColor(color: Color): String {
        return String.format("#%08X", color.toArgb())
    }

    @TypeConverter
    fun toColor(colorString: String): Color {
        return Color(colorString.toColorInt())
    }
}
