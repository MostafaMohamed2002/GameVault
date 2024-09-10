package com.mostafadevo.freegames.data.local.FreeGameDetails

import androidx.room.TypeConverter
import com.mostafadevo.freegames.data.remote.dto.MinimumSystemRequirements
import com.mostafadevo.freegames.data.remote.dto.Screenshot
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MinimumSystemRequirementsConverter {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun fromMinimumSystemRequirements(data: MinimumSystemRequirements?): String? {
        return moshi.adapter(MinimumSystemRequirements::class.java).toJson(data)
    }

    @TypeConverter
    fun toMinimumSystemRequirements(json: String?): MinimumSystemRequirements? {
        return json?.let {
            moshi.adapter(MinimumSystemRequirements::class.java).fromJson(it)
        }
    }
}

class ScreenshotListConverter {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val listType = Types.newParameterizedType(List::class.java, Screenshot::class.java)

    @TypeConverter
    fun fromScreenshotList(data: List<Screenshot>?): String? {
        return moshi.adapter<List<Screenshot>>(listType).toJson(data)
    }

    @TypeConverter
    fun toScreenshotList(json: String?): List<Screenshot>? {
        return json?.let {
            moshi.adapter<List<Screenshot>>(listType).fromJson(it)
        }
    }
}
