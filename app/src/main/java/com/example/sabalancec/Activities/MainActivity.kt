package com.example.sabalancec.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.sabalancec.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ensure you set the correct layout

        val btLoginTemp: Button = findViewById(R.id.bt_login_temp) // Correct button initialization

        btLoginTemp.setOnClickListener {
            val intent = Intent(this, WhenLoggedIn::class.java)
            startActivity(intent)
        }
    }
}
