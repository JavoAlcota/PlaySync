package com.udaproject.myapplication.views.youtubeAPI

data class YoutubeChannel(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val pageInfo: PageInfo
)