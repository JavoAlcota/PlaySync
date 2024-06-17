package com.udaproject.myapplication.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.udaproject.myapplication.R

class SpotifyLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val spotifyLoginBtn = findViewById<Button>(R.id.sfLoginBtn)
        spotifyLoginBtn.setOnClickListener {
            val builder = AuthorizationRequest.Builder(
                SpotifyConstants.CLIENTE_ID,
                AuthorizationResponse.Type.TOKEN,
                SpotifyConstants.REDIRECT_URI,
            )

            builder.setScopes(arrayOf("playlist-read-private",
                "user-read-email",
                "playlist-modify-public",
                "playlist-modify-private",
                ))


            val request = builder.build()

            AuthorizationClient.openLoginActivity(this, SpotifyConstants.REQUEST_CODE, request)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == SpotifyConstants.REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    SpotifyUserData.userToken = response.accessToken

                    Toast.makeText(this,"Authentication Ok!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, UserDataActivity::class.java)
                    startActivity(intent)

                }
                AuthorizationResponse.Type.ERROR -> {
                    Toast.makeText(this, "Authentication failed: ${response.error}", Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        }
    }
}
