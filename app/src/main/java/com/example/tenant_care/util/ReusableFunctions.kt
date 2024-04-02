package com.example.tenant_care.util

import java.text.NumberFormat
import java.util.Locale

object ReusableFunctions {
    val formattedRent = NumberFormat.getCurrencyInstance(Locale("en", "KE")).format(10.0)
    // enforce only email values to be entered
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // format currency values

    fun formatMoneyValue(amount: Double): String {
        return  NumberFormat.getCurrencyInstance(Locale("en", "KE")).format(amount)
    }
}