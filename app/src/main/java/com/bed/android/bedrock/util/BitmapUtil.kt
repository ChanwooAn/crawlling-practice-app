package com.bed.android.bedrock.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.net.URL

object BitmapUtil {

    fun getBitmapFromUrl(imageUrl: String): Bitmap? = try {
        val conn = URL(imageUrl).openConnection()
        conn.connect()
        val inputStream = conn.getInputStream()
        inputStream.mark(inputStream.available())
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        bitmap
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }

}
