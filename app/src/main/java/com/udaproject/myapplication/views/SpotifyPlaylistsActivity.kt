package com.udaproject.myapplication.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.udaproject.myapplication.R
import com.udaproject.myapplication.views.spotify_api.HeaderInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.udaproject.myapplication.views.spotifyPlaylists.SpotifyPlaylistAPI

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

        obtainUserPlaylist(playlistsContainer)
    }
    private fun getClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
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
//                val userData = call.body()
                SpotifyPlaylists.playlists = call.body()
                runOnUiThread {
                    if (call.isSuccessful && SpotifyPlaylists.playlists != null) {

                        for (playlist in SpotifyPlaylists.playlists?.items!!){
                            val playlistView = LayoutInflater.from(this@SpotifyPlaylistsActivity).inflate(R.layout.playlists_base, playlistsContainer, false)
                            val playlistButton = playlistView.findViewById<Button>(R.id.playlistButton)
                            val playlistStatus = playlistView.findViewById<TextView>(R.id.playlistStatus)
                            playlistButton.text = playlist.name

                            playlistButton.setOnClickListener{
                                CurrentPlaylist.playlistId = playlist.id
                                CurrentPlaylist.playlistName = playlist.name
//                                Toast.makeText(this@SpotifyPlaylistsActivity, "${CurrentPlaylist.playlistId}", Toast.LENGTH_SHORT).show()
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