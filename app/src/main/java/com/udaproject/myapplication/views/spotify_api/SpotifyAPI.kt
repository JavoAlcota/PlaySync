package com.udaproject.myapplication.views.spotify_api

import com.udaproject.myapplication.views.SpotifyUserData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface SpotifyAPI {
    @GET
    suspend fun getSpotifyUserData(@Url url:String):Response<User>
}