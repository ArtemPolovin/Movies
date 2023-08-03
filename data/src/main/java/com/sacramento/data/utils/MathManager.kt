package com.sacramento.data.utils

import kotlin.math.roundToInt

 fun roundFloat(number: Double?): Float? {
    return number?.let { (it * 10.0).roundToInt() / 10.0 }?.toFloat()
}