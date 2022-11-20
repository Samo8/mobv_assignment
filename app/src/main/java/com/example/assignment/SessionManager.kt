package com.example.assignment

import android.content.Context
import android.content.SharedPreferences
import com.example.assignment.auth.AuthData
import com.google.gson.Gson

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

    fun fetchAuthData(): AuthData {
        val authData = prefs?.getString(AUTH_DATA, null)
            ?: return AuthData(uid = "-1", access = "", refresh = "")

        return Gson().fromJson(authData, AuthData::class.java)
    }
}