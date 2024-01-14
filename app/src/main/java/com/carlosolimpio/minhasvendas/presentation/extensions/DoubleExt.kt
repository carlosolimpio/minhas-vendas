package com.carlosolimpio.minhasvendas.presentation.extensions

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

fun Double.toBRLCurrencyString(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR")) as DecimalFormat
    format.positivePrefix = ""
    return format.format(this)
}
