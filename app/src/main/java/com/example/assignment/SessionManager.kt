package com.example.assignment

import android.content.Context
import android.content.SharedPreferences
import com.example.assignment.data.models.AuthData
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

//class SessionManager @Inject constructor(@ApplicationContext context: Context?) {
class SessionManager(context: Context?) {
    private var prefs: SharedPreferences? = context?.getSharedPreferences(
        "BAR_APP",
        Context.MODE_PRIVATE
    )

    companion object {
        const val AUTH_DATA = "auth_data"
    }

    fun saveAuthData(authData: AuthData) {
        val serializedData = Gson().toJson(authData)
        prefs?.edit()?.putString(AUTH_DATA, serializedData)?.apply()
    }

    fun fetchAuthData(): AuthData? {
        val authData = prefs?.getString(AUTH_DATA, null) ?: return null
        return Gson().fromJson(authData, AuthData::class.java)
    }
}