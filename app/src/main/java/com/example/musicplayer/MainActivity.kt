package com.example.musicplayer

import android.Manifest
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.lang.Exception

public lateinit var currentSong: Song
public var songs : ArrayList<Song>? = null

class MainActivity : AppCompatActivity() {



    private lateinit var recyclerView: RecyclerView
    private var metadataRetriever = MediaMetadataRetriever()
    private var art = ByteArray(8192)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        songs = ArrayList<Song>()
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.navigationBarColor = ContextCompat.getColor(this, R.color.colorDarkGrey)
        }
        getPermission()

    }

    fun getPermission(){
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    display()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(applicationContext, "Permission not granted. Shutting Down", Toast.LENGTH_LONG).show()
                    finish()
                }
            }).check()
    }

    fun findSongs(file : File) : ArrayList<File>{
        var arrayList = ArrayList<File>()
        var files = file.listFiles()
        for(singleFile in files){
            if(singleFile.isDirectory && !singleFile.isHidden){
                arrayList.addAll(findSongs(singleFile))
            }
            else {
                if(singleFile.name.endsWith(".mp3") || singleFile.name.endsWith(".wav")){
                    arrayList.add(singleFile)
                }
            }
        }
        return arrayList
    }

    fun display(){
        val mySongs = findSongs(Environment.getExternalStorageDirectory())
        var i = 0
        for (song in mySongs) {
            metadataRetriever.setDataSource(mySongs.get(i).toString())
            try {
                art = metadataRetriever.getEmbeddedPicture();
                songs!!.add(Song(BitmapFactory.decodeByteArray(art, 0, art.size),
                    metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
                    metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST),
                    metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                    metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE), mySongs[i].toUri(),
                    metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)))
            }
            catch (e : Exception){
                songs!!.add(Song(null,
                    mySongs.get(i).name.toString().replace(".mp3", "").replace(".wav", ""), null, null, null, mySongs[i].toUri(), metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)))
            }
            i++
        }

        recyclerView = findViewById<RecyclerView>(R.id.songs)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL!!
        recyclerView.layoutManager = layoutManager

        val adapter = SongsAdapter(this, songs!!)
        recyclerView.adapter = adapter


    }

}
