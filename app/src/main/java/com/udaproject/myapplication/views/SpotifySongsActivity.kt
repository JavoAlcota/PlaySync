package com.udaproject.myapplication.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.TextToolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.udaproject.myapplication.R
import com.udaproject.myapplication.views.spotifyPlaylists.SpotifyPlaylistAPI
import com.udaproject.myapplication.views.spotifyTracks.SpotifyTracks
import com.udaproject.myapplication.views.spotifyTracks.SpotifyTracksAPI
import com.udaproject.myapplication.views.spotify_api.HeaderInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpotifySongsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spotify_songs)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tracksContainer = findViewById<LinearLayout>(R.id.songs)

        obtainUserPlaylistTracks(tracksContainer)
    }

    private fun getClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()
    private fun getSpotifyPlaylistTracks(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/playlists/${CurrentPlaylist.playlistId}/tracks/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
    }

    @SuppressLint("SetTextI18n")
    private fun obtainUserPlaylistTracks(tracksContainer: LinearLayout) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getSpotifyPlaylistTracks().create(SpotifyTracksAPI::class.java).getSpotifyUserPlaylistTracks("")
                SpotifyPlaylistTracks.tracks = call.body()
                runOnUiThread {
                    if (call.isSuccessful && SpotifyPlaylistTracks.tracks != null) {
                        val title = findViewById<TextView>(R.id.playlistName)
                        title.text = CurrentPlaylist.playlistName

                        for (song in SpotifyPlaylistTracks.tracks!!.items){
                            val songView = LayoutInflater.from(this@SpotifySongsActivity)
                                .inflate(R.layout.songs_base, tracksContainer, false)

                            val trackButton = songView.findViewById<Button>(R.id.songButton)
                            val trackStatus = songView.findViewById<TextView>(R.id.songStatus)

                            trackButton.text = song.track.name
                            trackButton.setOnClickListener{
                                Toast.makeText(this@SpotifySongsActivity, "${song.track.album.name}", Toast.LENGTH_SHORT).show()
                            }

                            tracksContainer.addView(songView)
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

