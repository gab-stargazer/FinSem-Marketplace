package com.lelestacia.finsem_market.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.launchUrlIntent(url: String) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )
    startActivity(intent)
}

fun Context.launchWhatsappIntent(number: String) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://wa.me/$number")
    )
    startActivity(intent)
}