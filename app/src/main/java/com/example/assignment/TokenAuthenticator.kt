package com.example.assignment
//
//import com.example.assignment.auth.AuthServer
//import com.example.assignment.auth.AuthService
//import com.example.assignment.user.UserService
//import okhttp3.Authenticator
//import okhttp3.Request
//import okhttp3.Response
//import okhttp3.Route
//
//class TokenAuthenticator() : Authenticator {
//    override fun authenticate(route: Route?, response: Response): Request? {
//        // This is a synchronous call
//        val updatedToken = getUpdatedToken()
//
//        return response.request().newBuilder()
//            .header(ApiClient.HEADER_AUTHORIZATION, updatedToken)
//            .build()
//    }
//
//    private fun getUpdatedToken(): String {
//        val requestParams = HashMap<String, String>()
//
//        AuthService.refreshToken()
//
//        val authTokenResponse = ApiClient.userApiService.getAuthenticationToken(requestParams).execute().body()!!
//
//        val newToken = "${authTokenResponse.tokenType} ${authTokenResponse.accessToken}"
//        SharedPreferenceUtils.saveString(Constants.PreferenceKeys.USER_ACCESS_TOKEN, newToken)
//        return newToken
//    }
//}