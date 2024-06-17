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
import com.udaproject.myapplication.R
import com.udaproject.myapplication.views.spotify_api.HeaderInterceptor
import com.udaproject.myapplication.views.spotify_api.SpotifyAPI
import com.udaproject.myapplication.views.spotify_api.User
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



        val ytUserName = findViewById<TextView>(R.id.ytUserName)
        val ytUserEmail = findViewById<TextView>(R.id.ytUserEmail)
        val sfUserName = findViewById<TextView>(R.id.sfUserName)
        val sfUserEmail = findViewById<TextView>(R.id.sfUserEmail)

        ytUserName.text = YoutubeUserData.userName
        ytUserEmail.text = YoutubeUserData.userEmail

        val continueBtn = findViewById<Button>(R.id.continueBtn)
        continueBtn.setOnClickListener{
            val intent = Intent(this, SpotifyPlaylistsActivity::class.java)
            startActivity(intent)
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

    private fun getClient():OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()


    private fun obtainUserData(sfUserName: TextView, sfUserEmail: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getUserDataSF().create(SpotifyAPI::class.java).getSpotifyUserData("me")
                val userData = call.body()
                runOnUiThread {
                    if (call.isSuccessful && userData != null) {
                        sfUserName.text = "${userData.display_name}"
                            if (userData.email == null){
                                sfUserEmail.text = "You don't have an email configured."
                            }else{
                                sfUserEmail.text = "${userData.email}"
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