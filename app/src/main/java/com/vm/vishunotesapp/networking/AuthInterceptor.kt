package com.vm.vishunotesapp.networking

import com.vm.vishunotesapp.utils.Constants
import com.vm.vishunotesapp.utils.UserTokenStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Intercepts and adds the "Authorization" header in the HttpRequest.
 */
class AuthInterceptor @Inject constructor(private val tokenStore: UserTokenStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.addHeader(Constants.HEADER_AUTHORIZATION, "Bearer ${tokenStore.getToken()}")
        return chain.proceed(request.build())
    }
}