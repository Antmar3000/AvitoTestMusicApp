package com.am.remote.data.api.models

import com.am.core.entity.Track

data class TrackResponse(
    val tracks : Data
)

data class Data(
    val data: List<Track>
)