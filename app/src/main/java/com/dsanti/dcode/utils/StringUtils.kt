package com.dsanti.dcode.utils

import android.util.Patterns

fun String.isValidUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()

fun String.isValidEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPhone(): Boolean = Patterns.PHONE.matcher(this).matches()
