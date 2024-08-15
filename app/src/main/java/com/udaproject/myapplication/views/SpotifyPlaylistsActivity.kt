package com.udaproject.myapplication.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.udaproject.myapplication.views.spotifyPlaylists.SpotifyPlaylistAPI
import com.udaproject.myapplication.views.spotify_api.SpotifyHeaderInterceptor

class SpotifyPlaylistsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spotify_playlists)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val playlistsContainer = findViewById<LinearLayout>(R.id.playlists)


        val youtubeBtn = findViewById<ImageButton>(R.id.ytBtn)
        youtubeBtn.setOnClickListener{
            val intent = Intent(this@SpotifyPlaylistsActivity, YoutubePlaylistsActivity::class.java)
            startActivity(intent)
        }

        val menu = findViewById<ImageButton>(R.id.menu)
        menu.setOnClickListener{
            val intent = Intent(this@SpotifyPlaylistsActivity, UserDataActivity::class.java)
            startActivity(intent)
        }

//============================================= PARCHE POR SI ACASO ==========================================================
        data class SFPlaylists(val title:String, val total:Int)

        val playlists = listOf(
            SFPlaylists("Top Hits", 20),
            SFPlaylists("Chill Vibes", 15),
            SFPlaylists("Rock Classics", 25),
            SFPlaylists("Hip Hop Essentials", 30),
            SFPlaylists("Jazz Favorites", 10),
            SFPlaylists("Workout Jams", 18),
            SFPlaylists("Indie Hits", 22),
            SFPlaylists("Electronic Beats", 14),
            SFPlaylists("Pop Hits", 28),
            SFPlaylists("Country Roads", 12),
            SFPlaylists("Reggae Vibes", 17),
            SFPlaylists("Classical Masterpieces", 8),
            SFPlaylists("Party Mix", 21),
            SFPlaylists("Relaxing Music", 13),
            SFPlaylists("Motivational Tracks", 19),
            SFPlaylists("Summer Favorites", 23),
            SFPlaylists("Ambient Sounds", 11),
            SFPlaylists("Soul Classics", 16),
            SFPlaylists("Alternative Rock", 27),
            SFPlaylists("RnB Essentials", 20),
            SFPlaylists("80s Hits", 24),
            SFPlaylists("Dance Party", 25),
            SFPlaylists("Movie Soundtracks", 14),
            SFPlaylists("Deep House", 15),
            SFPlaylists("Acoustic Sessions", 12),
            SFPlaylists("Guitar Legends", 18),
            SFPlaylists("Electronic Chill", 21),
            SFPlaylists("Live Performances", 22),
            SFPlaylists("Instrumental Beats", 13),
            SFPlaylists("K-Pop Hits", 19),
            SFPlaylists("Folk Tunes", 16)
        )
        for (playlist in playlists){
            val playlistView = LayoutInflater.from(this@SpotifyPlaylistsActivity).inflate(R.layout.playlists_base, playlistsContainer, false)
            val playlistButton = playlistView.findViewById<Button>(R.id.playlistButton)
            val playlistStatus = playlistView.findViewById<TextView>(R.id.playlistStatus)
            playlistButton.text = playlist.title + " (" + playlist.total + ")"

            //playlistButton.text = playlist.tracks.total.toString()

            playlistButton.setOnClickListener{
                CurrentPlaylist.playlistName = playlist.title
                val intent = Intent(this@SpotifyPlaylistsActivity, SpotifySongsActivity::class.java)
                startActivity(intent)
            }
            playlistsContainer.addView(playlistView)
        }

//==========================================================================================================================================



        //obtainUserPlaylist(playlistsContainer)
    }
    private fun getClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(SpotifyHeaderInterceptor())
            .build()

    private fun getUserPlaylistSF(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/me/playlists/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
    }

    @SuppressLint("SetTextI18n")
    private fun obtainUserPlaylist(playlistsContainer: LinearLayout) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getUserPlaylistSF().create(SpotifyPlaylistAPI::class.java).getSpotifyUserPlaylists("")
                SpotifyPlaylists.playlists = call.body()
                runOnUiThread {
                    if (call.isSuccessful && SpotifyPlaylists.playlists != null) {

                        for (playlist in SpotifyPlaylists.playlists?.items!!){
                            val playlistView = LayoutInflater.from(this@SpotifyPlaylistsActivity).inflate(R.layout.playlists_base, playlistsContainer, false)
                            val playlistButton = playlistView.findViewById<Button>(R.id.playlistButton)
                            val playlistStatus = playlistView.findViewById<TextView>(R.id.playlistStatus)
                            playlistButton.text = playlist.name + " (" + playlist.tracks.total.toString() + ")"

                            //playlistButton.text = playlist.tracks.total.toString()

                            playlistButton.setOnClickListener{
                                CurrentPlaylist.playlistId = playlist.id
                                CurrentPlaylist.playlistName = playlist.name
                                val intent = Intent(this@SpotifyPlaylistsActivity, SpotifySongsActivity::class.java)
                                startActivity(intent)
                            }
                            playlistsContainer.addView(playlistView)
                        }
                    } else {
                        val errorBody = call.errorBody()?.string()
                        Log.e("UserDataActivity", "Error in API call: $errorBody")
                        showError("Error in API call: $errorBody")
                    }
                }
            } catch (e: Exception) {
                Log.e("UserDataActivity", "Exception during API call", e)
                runOnUiThread { showError(e.message) }
            }
        }
    }

    private fun showError(message: String?) {
        Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
    }
}