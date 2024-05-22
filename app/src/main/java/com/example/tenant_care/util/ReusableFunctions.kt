package com.example.tenant_care.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tenant_care.datastore.UserDSDetails
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object ReusableFunctions {
    val formattedRent = NumberFormat.getCurrencyInstance(Locale("en", "KE")).format(10.0)
    // enforce only email values to be entered
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    data class UserDetails(
        val roleId: Int? = 0,
        val userId: Int? = 0,
        val fullName: String = "",
        val email: String = "",
        val userAddedAt: String = "",
        val phoneNumber: String = "",
        val room: String = "",
        val password: String = ""
    )

    // format currency values

    fun formatMoneyValue(amount: Double): String {
        return  NumberFormat.getCurrencyInstance(Locale("en", "KE")).format(amount)
    }

    // userUDDetails to UserDetails

    fun UserDSDetails.toUserDetails(): UserDetails = UserDetails(
        userId = userId,
        roleId = roleId,
        fullName = fullName,
        email = email,
        userAddedAt = userAddedAt,
        phoneNumber = phoneNumber,
        room = room,
        password = password
    )

    // format datetime value

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateTimeValue(dateTime: String): String {
        val dateTimeParts = dateTime.split("T")
        val datePart = dateTimeParts[0] // yyyy-MM-dd
        val timePart = dateTimeParts[1].substring(0, 5) // HH:mm
        return "$datePart $timePart"
    }




}