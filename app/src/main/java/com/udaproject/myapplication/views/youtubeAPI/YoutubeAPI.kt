package com.udaproject.myapplication.views.youtubeAPI

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface YoutubeAPI {
    @GET
    suspend fun getYoutubeChannelData(@Url url:String): Response<YoutubeChannel>
}