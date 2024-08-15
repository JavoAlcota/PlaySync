package com.udaproject.myapplication.views.youtubeAPI

data class Snippet(
    val customUrl: String,
    val description: String,
    val localized: Localized,
    val publishedAt: String,
    val thumbnails: Thumbnails,
    val title: String
)