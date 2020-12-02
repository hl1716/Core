package com.hviewtech.wowpay.merchant.core.net

import android.util.Log
import com.hviewtech.wowpay.merchant.core.utils.PreferenceUtil
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()

        val url: HttpUrl = original.url().newBuilder()
                .addQueryParameter("token", PreferenceUtil.loadToken())
                .build()

        val request: Request = original.newBuilder()
                .addHeader("Content-Type", "multipart/form-data")
                .addHeader("Connection", "keep-alive")
                .method(original.method(), original.body())
                .url(url)
                .build()
        Log.i("http-url", request.url().toString())
        return chain.proceed(request)
    }

}