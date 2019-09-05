package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.Image
import android.media.SoundPool
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class SongsAdapter (val context: Context, private val songs : ArrayList<Song>) : RecyclerView.Adapter<SongsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.list_item,parent,false)
            return ViewHolder(v)
        }

        override fun getItemCount(): Int {
            return songs.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val song = songs[position]


            holder?.songName.isSelected = true
            holder?.songName.text = song.getName()

            if(song.getImage()!=null) {
                holder?.wordImage.setImageBitmap(song.getImage()!!)
            }
            else
                holder?.wordImage.setImageResource(R.drawable.playingsongimage)

            holder?.relativeLayout.setOnClickListener {
                var intent = Intent(context, PlayMusicActivity ::class.java).putExtra("pos",position)
                currentSong = song
                context.startActivity(intent)
            }

        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val songName = itemView.findViewById(R.id.song_name) as TextView
            val wordImage = itemView.findViewById(R.id.songImage) as ImageView
            val relativeLayout = itemView.findViewById(R.id.relativeLayout) as RelativeLayout
        }
    }