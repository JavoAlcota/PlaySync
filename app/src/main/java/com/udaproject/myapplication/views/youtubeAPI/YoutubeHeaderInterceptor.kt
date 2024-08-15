package com.udaproject.myapplication.views.youtubeAPI


import com.udaproject.myapplication.views.YoutubeUserData
import okhttp3.Interceptor
import okhttp3.Response

class YoutubeHeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(
            "Authorization", "Bearer ${YoutubeUserData.userToken}"
            ).addHeader(
                "Accept", "application/json"
            ).build()
        return chain.proceed(request)
    }
}