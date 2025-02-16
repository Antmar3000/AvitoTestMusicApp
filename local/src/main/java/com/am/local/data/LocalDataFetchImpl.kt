package com.am.local.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.am.core.entity.Album
import com.am.core.entity.Artist
import com.am.core.entity.Track
import com.am.local.data.local.LocalDataFetch
import javax.inject.Inject


class LocalDataFetchImpl @Inject constructor (
    private val context: Context
) : LocalDataFetch {

    private var cursor: Cursor? = null

    private var projection: Array<String> = arrayOf(
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
        MediaStore.Audio.AudioColumns.ARTIST,
        MediaStore.Audio.AudioColumns.DATA,
        MediaStore.Audio.AudioColumns.DURATION,
        MediaStore.Audio.AudioColumns.TITLE,
        MediaStore.Audio.AudioColumns.ALBUM_ID,
        MediaStore.Audio.AudioColumns.ALBUM,
        MediaStore.Audio.AudioColumns.ARTIST_ID
    )

    private val selection: String = "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ?"
    private val selectionArg = arrayOf("1")
    private val sortOrder = "${MediaStore.Audio.AudioColumns.DISPLAY_NAME} ASC"

    private fun getCoverUri(albumId: Long): Uri {
        return Uri.parse("content://media/external/audio/albumart/$albumId")
    }

    override fun getAudioData(): List<Track> {
        val musicList = mutableListOf<Track>()
        cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArg,
            sortOrder
        )

        cursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)

            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)

            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)

            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)

            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)

            val albumNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)

            val artistIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID)



            cursor.apply {
                if (count == 0) {
                    Log.i("myLog", "noData")
                } else {
                    while (cursor.moveToNext()) {

                        val id = getLong(idColumn)
                        val artistId = getLong(artistIdColumn)
                        val artistName = getString(artistColumn)
                        val duration = getInt(durationColumn)
                        val title = getString(titleColumn)
                        val albumId = getLong(albumIdColumn)
                        val albumName = getString(albumNameColumn)
                        val uri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            getLong(idColumn)
                        )

                        musicList += Track(
                            id = id,
                            title = title,
                            artist = Artist(artistId, artistName),
                            duration = duration,
                            preview = uri,
                            album = Album(albumId, albumName, getCoverUri(albumId))
                        )
                    }
                }
            }
        }
        return musicList
    }
}



