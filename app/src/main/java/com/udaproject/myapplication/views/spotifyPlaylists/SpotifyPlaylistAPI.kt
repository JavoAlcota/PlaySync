package com.udaproject.myapplication.views.spotifyPlaylists

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface SpotifyPlaylistAPI {
    @GET
    suspend fun getSpotifyUserPlaylists(@Url url:String): Response<SpotifyPlaylists>
}