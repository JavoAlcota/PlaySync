package com.udaproject.myapplication.views.spotifyPlaylists

data class SpotifyPlaylists(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: String,
    val total: Int
)