package com.example.musicplayer

import android.graphics.PorterDuff
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import java.lang.Long

import android.widget.TextView
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_play_music.*
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit

class PlayMusicActivity : AppCompatActivity() {

    //UI Components
    private lateinit var songImage : ImageView
    private lateinit var songName : TextView
    private lateinit var songSeekBar: SeekBar
    private lateinit var updateSeekBar: Thread
    private lateinit var btn_pause : Button
    private lateinit var btn_next : Button
    private lateinit var btn_previous : Button
    private lateinit var duration : TextView
    private lateinit var currentDuration : TextView
    private lateinit var artistName : TextView

    //variables
    private var position = 0
    companion object{
        var mediaPlayer = MediaPlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)

        init()

        var songNameString  = currentSong.getName()


        var bundle = intent.extras
        position = bundle.getInt("pos", 0)

        if (currentSong.getImage() != null) {
            songImage.setBackgroundResource(R.color.colorBlack)
            songImage.setImageBitmap(currentSong.getImage()!!)
        }
        else
            songImage.setImageResource(R.drawable.playingsongimage)


        var utils = Utilities()

        songName.setText(songNameString)
        songName.isSelected = true

        if(currentSong.getArtist() != null){
            artistName.setText(currentSong.getArtist())
            artistName.isSelected = true
            artistName.isVisible = true
        }
        else
            artistName.isVisible = false

        updateSeekBar = Thread(Runnable {
            var totalDuration = mediaPlayer.duration
            var currentPosition =  0


            while (currentPosition < totalDuration) {
                try {
//                  Add UI thread here
                    runOnUiThread(Runnable {
                        totalDuration = mediaPlayer.duration
                        currentPosition = mediaPlayer.currentPosition
                        songSeekBar.setProgress(currentPosition)
                        currentDuration.setText(""+utils.milliSecondsToTimer((currentPosition).toLong()))
                        duration.setText(""+utils.milliSecondsToTimer(totalDuration.toLong()))
                    });
                    sleep(500)
                }
                catch (e : InterruptedException){
                    e.printStackTrace()
                }
            }

        })

        if(mediaPlayer!=null){
            mediaPlayer.stop()
            mediaPlayer.release()
        }


        mediaPlayer = MediaPlayer.create(this, currentSong.getPosition())
        mediaPlayer.start()


        songSeekBar.max = mediaPlayer.duration
        updateSeekBar.start()

        songSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if(seekBar!!.progress == seekBar!!.max)
                    nextSong()
                else
                    mediaPlayer.seekTo(seekBar!!.progress)
            }
        })

        mediaPlayer.setOnCompletionListener {
            nextSong()
        }

        btn_pause.setOnClickListener(View.OnClickListener {
            songSeekBar.max = mediaPlayer.duration

            if(mediaPlayer.isPlaying)
            {
                btn_pause.setBackgroundResource(R.drawable.ic_action_play)
                mediaPlayer.pause()
            }
            else
            {
                btn_pause.setBackgroundResource(R.drawable.ic_pause_green_24dp)
                mediaPlayer.start()
            }
        })


        btn_next.setOnClickListener(View.OnClickListener {
            nextSong()
        })

        btn_previous.setOnClickListener(View.OnClickListener {
            previousSong()
        })


    }

    /*
        Function to initialize all UI variables/components
    */
    fun init(){
        songSeekBar = findViewById(R.id.seekBar)
        songImage = findViewById(R.id.songImage) as ImageView
        songName = findViewById(R.id.songName) as TextView
        btn_pause = findViewById(R.id.playButton) as Button
        btn_next = findViewById(R.id.nextSongButton) as Button
        btn_previous = findViewById(R.id.previousSongButton) as Button
        duration = findViewById(R.id.totalDuration)
        currentDuration = findViewById(R.id.currentDuration)
        artistName = findViewById(R.id.artistName)

        songSeekBar.getProgressDrawable().setColorFilter(resources.getColor(R.color.colorWhite), PorterDuff.Mode.MULTIPLY);
        songSeekBar.getThumb().setColorFilter(resources.getColor(R.color.colorWhite), PorterDuff.Mode.SRC_IN);
    }

    /*
        Function to play next song
    */
    fun nextSong(){
        mediaPlayer.stop()
        mediaPlayer.release()
        position++
        var size = songs!!.size
        position = (position % size)
        currentSong = songs!![position]
        mediaPlayer = MediaPlayer.create(this, currentSong.getPosition())

        songName.setText(currentSong.getName())
        songName.isSelected = true
        if(currentSong.getArtist() != null){
            artistName.setText(currentSong.getArtist())
            artistName.isSelected = true
            artistName.isVisible = true
        }
        else
            artistName.isVisible = false
        if (currentSong.getImage() != null) {
            songImage.setBackgroundResource(R.color.colorBlack)
            songImage.setImageBitmap(currentSong.getImage()!!)
        }
        else
            songImage.setImageResource(R.drawable.playingsongimage)
        songSeekBar.max = mediaPlayer.duration
        mediaPlayer.start()
    }

    /*
        Function to play previous song
    */
    fun previousSong(){
        mediaPlayer.stop()
        mediaPlayer.release()
        var size = songs!!.size
        if(position != 0)
        {
            position--
        }
        else
        {
            position = size - 1
        }

        position = (position % size)
        currentSong = songs!![position]
        mediaPlayer = MediaPlayer.create(this, currentSong.getPosition())

        songName.setText(currentSong.getName())
        songName.isSelected = true
        if(currentSong.getArtist() != null){
            artistName.setText(currentSong.getArtist())
            artistName.isSelected = true
            artistName.isVisible = true
        }
        else
            artistName.isVisible = false
        if (currentSong.getImage() != null) {
            songImage.setBackgroundResource(R.color.colorBlack)
            songImage.setImageBitmap(currentSong.getImage()!!)
        }
        else
            songImage.setImageResource(R.drawable.playingsongimage)
        songSeekBar.max = mediaPlayer.duration
        mediaPlayer.start()
    }
}
