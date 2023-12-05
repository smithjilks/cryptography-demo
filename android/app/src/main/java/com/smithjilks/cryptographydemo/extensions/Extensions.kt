package com.smithjilks.cryptographydemo.extensions

import android.util.Patterns

fun String.isStringDigitsOnly(): Boolean {
    return this.isNotEmpty() && this.all { it.isDigit() }
}

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
