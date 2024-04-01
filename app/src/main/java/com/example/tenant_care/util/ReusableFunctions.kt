package com.example.tenant_care.util

object ReusableFunctions {
    // enforce only email values to be entered
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}