package com.example.mynote1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class splash : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}