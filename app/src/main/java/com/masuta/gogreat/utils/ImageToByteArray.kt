package com.masuta.gogreat.utils

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun imageBitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}