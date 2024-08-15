package com.udaproject.myapplication.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.udaproject.myapplication.R

class YoutubeSongsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_youtube_songs)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        data class Item(val title:String, val artist:String)


        val playlistName = findViewById<TextView>(R.id.playlistName)


        playlistName.text = CurrentPlaylist.playlistName

        val menu = findViewById<ImageButton>(R.id.menu)
        menu.setOnClickListener{
            val intent = Intent(this@YoutubeSongsActivity, UserDataActivity::class.java)
            startActivity(intent)
        }


        val youtubeBtn = findViewById<ImageButton>(R.id.ytBtn)
        youtubeBtn.setOnClickListener{
            val intent = Intent(this@YoutubeSongsActivity, YoutubePlaylistsActivity::class.java)
            startActivity(intent)
        }

        val spotifyBtn = findViewById<ImageButton>(R.id.sfBtn)
        spotifyBtn.setOnClickListener{
            val intent = Intent(this@YoutubeSongsActivity, SpotifyPlaylistsActivity::class.java)
            startActivity(intent)
        }


        fun listTracks(tracks: List<Item>){
            for (track in tracks){
                val tracksContainer = findViewById<LinearLayout>(R.id.songs)
                val trackView = LayoutInflater.from(this@YoutubeSongsActivity).inflate(R.layout.songs_base, tracksContainer, false)
                val trackButton = trackView.findViewById<Button>(R.id.songButton)
                val trackStatus = trackView.findViewById<TextView>(R.id.songStatus)
                trackButton.text = track.title
                tracksContainer.addView(trackView)
            }
        }


        if (CurrentPlaylist.playlistName == "relajao ykpz"){
            val tracks = listOf(
                Item("Whitewash", "Buckethead"),
                Item("Watching the Boats with My Dad", "Buckethead"),
                Item("Sunset on the Golden Age", "Buckethead"),
                Item("Weightless", "Marconi Union"),
                Item("Spiegel im Spiegel", "Arvo Pärt"),
                Item("Clair de Lune", "Claude Debussy"),
                Item("Gymnopédie No. 1", "Erik Satie"),
                Item("Echoes", "Pink Floyd"),
                Item("An Ending (Ascent)", "Brian Eno"),
                Item("Aqueous Transmission", "Incubus"),
                Item("Pavane pour une infante défunte", "Maurice Ravel"),
                Item("Nuvole Bianche", "Ludovico Einaudi"),
                Item("Requiem for a Dream", "Clint Mansell"),
                Item("Meditation", "Jules Massenet"),
                Item("Merry Christmas Mr. Lawrence", "Ryuichi Sakamoto"),
                Item("The Heart Asks Pleasure First", "Michael Nyman"),
                Item("Sappho", "Kronos Quartet"),
                Item("Prelude in C Major", "Johann Sebastian Bach"),
                Item("Blue in Green", "Miles Davis"),
                Item("Nocturne Op. 9 No. 2", "Frédéric Chopin"),
                Item("Cumbia Sobre el Río", "Celso Piña"),
                Item("Fly Me to the Moon", "Frank Sinatra"),
                Item("Sarabande", "Georg Friedrich Handel")
            )
            listTracks(tracks)
        }else if (CurrentPlaylist.playlistName == "Random songs"){
            val tracks = listOf(
                Item("Shape of You", "Ed Sheeran"),
                Item("Hallelujah", "Leonard Cohen"),
                Item("Billie Jean", "Michael Jackson"),
                Item("Bohemian Rhapsody", "Queen"),
                Item("Hotel California", "Eagles"),
                Item("Lose Yourself", "Eminem"),
                Item("Wonderwall", "Oasis"),
                Item("Piano Man", "Billy Joel")
            )
            listTracks(tracks)
        }else if (CurrentPlaylist.playlistName == "chill"){
            val tracks = listOf(
                Item("Weightless", "Marconi Union"),
                Item("Intro", "The xx"),
                Item("Sunset Lover", "Petit Biscuit"),
                Item("Island In The Sun", "Weezer"),
                Item("Holocene", "Bon Iver"),
                Item("Breathe", "Telepopmusik"),
                Item("To Build a Home", "The Cinematic Orchestra"),
                Item("Teardrop", "Massive Attack")
            )
            listTracks(tracks)
        }else if (CurrentPlaylist.playlistName == "retro music"){
            val tracks = listOf(
                Item("Take On Me", "a-ha"),
                Item("Don't You Want Me", "The Human League"),
                Item("Sweet Dreams (Are Made of This)", "Eurythmics"),
                Item("Africa", "Toto"),
                Item("Come On Eileen", "Dexys Midnight Runners"),
                Item("Every Breath You Take", "The Police"),
                Item("Girls Just Want to Have Fun", "Cyndi Lauper"),
                Item("Livin' on a Prayer", "Bon Jovi")
            )
            listTracks(tracks)
        }
    }
}