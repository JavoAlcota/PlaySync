package com.udaproject.myapplication.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.spotify.sdk.android.auth.AuthorizationClient
import com.udaproject.myapplication.R
import com.udaproject.myapplication.views.spotify_api.SpotifyAPI
import com.udaproject.myapplication.views.spotify_api.SpotifyHeaderInterceptor
import com.udaproject.myapplication.views.spotify_api.User
import com.udaproject.myapplication.views.youtubeAPI.YoutubeAPI
import com.udaproject.myapplication.views.youtubeAPI.YoutubeHeaderInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UserDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //User info
        val ytUserName = findViewById<TextView>(R.id.ytUserName)
        val ytUserEmail = findViewById<TextView>(R.id.ytUserEmail)
        val sfUserName = findViewById<TextView>(R.id.sfUserName)
        val sfUserEmail = findViewById<TextView>(R.id.sfUserEmail)

        //Buttons
        val endSessionBtn = findViewById<Button>(R.id.logOutBtn)
        val continueBtn = findViewById<Button>(R.id.continueBtn)

        ytUserName.text = YoutubeUserData.userName
        ytUserEmail.text = YoutubeUserData.userEmail

        endSessionBtn.setOnClickListener{
            AuthorizationClient.clearCookies(this)
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, YTLoginActivity::class.java)
            startActivity(intent)
        }

        continueBtn.setOnClickListener{
            finish()
        }

        obtainUserData(sfUserName, sfUserEmail)
    }

    private fun getUserDataSF():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
    }

    private fun getChannelDataSF():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://youtube.googleapis.com/youtube/v3/channels?")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getYoutubeClient())
            .build()
    }

    private fun getYoutubeClient():OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(YoutubeHeaderInterceptor())
            .build()


    private fun getClient():OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(SpotifyHeaderInterceptor())
            .build()


    private fun obtainUserData(sfUserName: TextView, sfUserEmail: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getUserDataSF().create(SpotifyAPI::class.java).getSpotifyUserData("me")
                val userData = call.body()
                runOnUiThread {
                    if (call.isSuccessful && userData != null) {
                        sfUserName.text = userData.display_name
                        sfUserEmail.text = userData.email
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