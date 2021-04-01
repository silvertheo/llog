package com.theo.llog.ext

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

internal val Number.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

internal val Context.screenWidth: Int
    get() = this.resources.displayMetrics.widthPixels

internal val Context.screenHeight: Int
    get() = this.resources.displayMetrics.heightPixels