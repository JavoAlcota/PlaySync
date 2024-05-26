package com.udaproject.myapplication.views

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.udaproject.myapplication.R

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

        sfUserName.text = "Spotify User's Token: ${SpotifyUserData.userToken}"
//        sfUserEmail.text = "Youtube User's Token: ${YoutubeUserData.userToken}"
    }
}