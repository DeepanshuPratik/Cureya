package com.example.cureya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Work flow is currently at [ChatBotFragment]
         */
        val intent = Intent(this, ChatBotFragment::class.java)
        this.startActivity(intent)
    }
}