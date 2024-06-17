package com.udaproject.myapplication.views.spotifyTracks

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface SpotifyTracksAPI {
    @GET
    suspend fun getSpotifyUserPlaylistTracks(@Url url:String): Response<SpotifyTracks>
}