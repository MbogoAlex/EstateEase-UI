package com.example.tenant_care.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DSRepository(
    private val datastore: DataStore<Preferences>
) {
    // pManager details
    private companion object {
        private val ROLE_ID = intPreferencesKey("roleId")
        private val USER_ID = intPreferencesKey("userId")
        private val USER_NAME = stringPreferencesKey("userName")
        private val USER_EMAIL = stringPreferencesKey("userEmail")
        private val PHONE_NUMBER = stringPreferencesKey("phoneNumber")
        private val USER_ADDED_AT = stringPreferencesKey("userAddedAt")
        private val ROOM = stringPreferencesKey("room")
        private val PASSWORD = stringPreferencesKey("password")
    }

    suspend fun saveUserDetails(
        userDSDetails: UserDSDetails
    ) {
        datastore.edit { preferences ->
            preferences[ROLE_ID] = userDSDetails.roleId!!
            preferences[USER_ID] = userDSDetails.userId!!
            preferences[USER_NAME] = userDSDetails.fullName
            preferences[USER_EMAIL] = userDSDetails.email
            preferences[PHONE_NUMBER] = userDSDetails.phoneNumber
            preferences[USER_ADDED_AT] = userDSDetails.userAddedAt
            preferences[ROOM] = userDSDetails.room
            preferences[PASSWORD] = userDSDetails.password
        }
    }

    val userDSDetails: Flow<UserDSDetails> = datastore.data
        .catch {
            if(it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            it.toUserDSDetails(
                roleId = it[ROLE_ID] ?: null,
                userId = it[USER_ID] ?: null,
                fullName = it[USER_NAME] ?: "",
                email = it[USER_EMAIL] ?: "",
                phoneNumber = it[PHONE_NUMBER] ?: "",
                userAddedAt = it[USER_ADDED_AT] ?: "",
                room = it[ROOM] ?: "",
                password = it[PASSWORD] ?: ""
            )
        }

    private fun Preferences.toUserDSDetails(
        roleId: Int?,
        userId: Int?,
        fullName: String,
        email: String,
        userAddedAt: String,
        phoneNumber: String,
        room: String,
        password: String
    ): UserDSDetails = UserDSDetails(
        roleId = roleId,
        userId = userId,
        fullName = fullName,
        email = email,
        userAddedAt = userAddedAt,
        phoneNumber = phoneNumber,
        room = room,
        password = password

    )

    suspend fun deleteAllPreferences() {
        datastore.edit {
            it.clear()
        }
    }

}