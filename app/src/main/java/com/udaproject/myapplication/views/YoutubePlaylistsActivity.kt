package com.udaproject.myapplication.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.udaproject.myapplication.R

class YoutubePlaylistsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_youtube_playlists)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val playlistsContainer = findViewById<LinearLayout>(R.id.playlists)


        val menu = findViewById<ImageButton>(R.id.menu)
        menu.setOnClickListener{
            val intent = Intent(this@YoutubePlaylistsActivity, UserDataActivity::class.java)
            startActivity(intent)
        }

        val spotifyBtn = findViewById<ImageButton>(R.id.sfBtn)
        spotifyBtn.setOnClickListener{
            val intent = Intent(this@YoutubePlaylistsActivity, SpotifyPlaylistsActivity::class.java)
            startActivity(intent)
        }

        val playlists: List<String> = listOf("relajao ykpz", "chill", "Random songs", "retro music")
        for (playlist in playlists){
            val playlistView = LayoutInflater.from(this@YoutubePlaylistsActivity).inflate(R.layout.playlists_base, playlistsContainer, false)
            val playlistButton = playlistView.findViewById<Button>(R.id.playlistButton)
            val playlistStatus = playlistView.findViewById<TextView>(R.id.playlistStatus)
            playlistButton.text = playlist

            playlistButton.setOnClickListener{
                CurrentPlaylist.playlistName = playlist
//                Toast.makeText(this@YoutubePlaylistsActivity, "${CurrentPlaylist.playlistName}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@YoutubePlaylistsActivity, YoutubeSongsActivity::class.java)
                startActivity(intent)
            }
            playlistsContainer.addView(playlistView)
        }

    }
}