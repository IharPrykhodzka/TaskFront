package me.kvait.mytodolist.api

import okhttp3.Interceptor
import okhttp3.Response
import java.lang.Exception

const val AUTH_TOKEN_HEADER = "Authorization"

class InjectAuthTokenInterceptor(val authToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithToken = originalRequest.newBuilder()
            .header(AUTH_TOKEN_HEADER, "Bearer $authToken")
            .build()
        return chain.proceed(requestWithToken)
    }
}
