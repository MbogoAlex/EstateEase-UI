package com.example.tenant_care.model.additionalExpense

import kotlinx.serialization.Serializable

@Serializable
data class AdditionalExpenseUpdateRequestBody(
    val name: String,
    val cost: Double
)
@Serializable
data class AdditionalExpenseResponseBody(
    val statusCode: Int,
    val message: String,
    val data: AdditionalExpenseData
)
@Serializable
data class AdditionalExpenseData(
    val expense: ExpenseDT
)
@Serializable
data class ExpenseDT(
    val id: Int,
    val name: String,
    val cost: Double
)
