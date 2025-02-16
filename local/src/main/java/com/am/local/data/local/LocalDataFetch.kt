package com.am.local.data.local

import com.am.core.entity.Track

interface LocalDataFetch {
    fun getAudioData() : List<Track>
}
