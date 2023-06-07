package com.example.vkapistart.models

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class VKAudio(
    val id: Long = 0,
    val ownerId: Long = 0,
    val artist: String = "",
    val title: String = "",
    //длительность в секундах
    val duration: Int = 0,
    val url: String = "",
    //идентификатор текста
    val lyricsId: Long = 0,
    //ид альбома если есть
    val albumId: Long = 0,
    //ид жанра
    val genreId: Long = 0,
    val date: String = "",
    //1, если включена опция «Не выводить при поиске». Если опция отключена, поле не возвращается
    val noSearch: Int = 0,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(ownerId)
        parcel.writeString(artist)
        parcel.writeString(title)
        parcel.writeInt(duration)
        parcel.writeString(url)
        parcel.writeLong(lyricsId)
        parcel.writeLong(albumId)
        parcel.writeLong(genreId)
        parcel.writeString(date)
        parcel.writeInt(noSearch)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VKAudio> {
        override fun createFromParcel(parcel: Parcel): VKAudio {
            return VKAudio(parcel)
        }

        override fun newArray(size: Int): Array<VKAudio?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject) = VKAudio(
            id = json.optLong("id", 0),
            ownerId = json.optLong("owner_id", 0),
            artist = json.optString("artist", ""),
            title = json.optString("title", ""),
            duration = json.optInt("duration", 0),
            url = json.optString("url", ""),
            lyricsId = json.optLong("lyrics_id", 0),
            albumId = json.optLong("album_id", 0),
            genreId = json.optLong("genre_id", 0),
            date = json.optString("date", ""),
            noSearch = json.optInt("no_search", 0)
        )
    }


}

//сделать enum жанров