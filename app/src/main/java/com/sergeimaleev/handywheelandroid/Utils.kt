package com.sergeimaleev.handywheelandroid

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import kotlin.math.roundToInt


object Utils {

    fun textAsBitmap(text: String, textSize: Float, textColor: Int): Bitmap? {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        paint.color = textColor
        paint.textAlign = Paint.Align.LEFT
        val baseline: Float = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(text) + 0.5f).roundToInt() // round
        val height = (baseline + paint.descent() + 0.5f).roundToInt()
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawText(text, 0F, baseline, paint)
        return image
    }

    fun Context?.getDrawableFromRes(resId: Int): Drawable? {
        if (this == null) return null
        return ResourcesCompat.getDrawable(resources, resId, theme)
    }
}