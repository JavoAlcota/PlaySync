package com.udaproject.myapplication.views.spotify_api

import com.udaproject.myapplication.views.SpotifyUserData
import okhttp3.Interceptor
import okhttp3.Response

class SpotifyHeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader(
            "Authorization", "Bearer ${SpotifyUserData.userToken}"
        ).build()
        return chain.proceed(request)
    }
}