package com.example.musicplayer

import android.graphics.Bitmap
import android.net.Uri
import java.time.Duration

class Song {
    private var image : Bitmap? = null
    private var name : String? = null
    private var artist : String? = null
    private var album : String? = null
    private var genre : String? = null
    private var position : Uri? = null
    private var duration : String? = null

    constructor(image: Bitmap?, name: String?, artist: String?, album: String?, genre: String?, position: Uri?, duration: String?) {
        this.image = image
        this.name = name
        this.artist = artist
        this.album = album
        this.genre = genre
        this.position = position
        this.duration = duration
    }

    fun getImage(): Bitmap? {
        return image
    }

    fun getName(): String? {
        return name
    }

    fun getArtist(): String? {
        return artist
    }

    fun getAlbum(): String? {
        return album
    }

    fun getGenre(): String? {
        return genre
    }

    fun getPosition() : Uri? {
        return position
    }

    fun getDuration() : String? {
        return duration
    }

}