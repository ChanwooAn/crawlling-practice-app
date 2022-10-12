package com.bed.android.bedrock.util

import android.text.format.DateFormat
import java.util.*

object TextUtils {

    private const val DATE_FORMAT = "yyyy. MM. dd"

    fun getFormattedDate(): String {
        return DateFormat.format(DATE_FORMAT, Calendar.getInstance().time).toString()
    }
}
