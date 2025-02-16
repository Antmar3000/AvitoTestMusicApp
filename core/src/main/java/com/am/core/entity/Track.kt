package com.am.core.entity

import android.net.Uri

data class Track (
    val id: Long,
    val title: String,
    val duration: Int,
    val preview: Uri,
    val artist: Artist,
    val album: Album
)

data class Artist(
    val id: Long,
    val name: String
)

data class Album(
    val id: Long,
    val title: String,
    val cover: Uri
)