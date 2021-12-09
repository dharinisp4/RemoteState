package com.example.assignment.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import android.graphics.Bitmap
import android.graphics.Canvas

import android.graphics.drawable.BitmapDrawable

import android.graphics.drawable.Drawable
import android.util.Log


object Utils {

    fun currentTimeStamp(): Long? {
       return System.currentTimeMillis() / 1000
    }

    fun getDate(): String {
        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("dd MMM yyyy HH:mm a", Locale.getDefault())
        return df.format(c)
    }


    @SuppressLint("SimpleDateFormat")
    fun createDatefromTimeStamp(timestamp: String?): String {
        var s = ""
        if (timestamp == null || timestamp.equals("null",false)) {
            return s
        } else {
            var dv: Long = if (timestamp.length > 10)
                timestamp.toLong()
            else
                java.lang.Long.valueOf(timestamp) * 1000
            val df = Date(dv)
            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm a", Locale.getDefault())
            Log.e("datessss", sdf.format(df).toString())
            s = sdf.format(df).toString()
        }
        return s
    }


    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }


}