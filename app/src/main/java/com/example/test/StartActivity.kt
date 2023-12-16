package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        BottomNavigationHelper(this, bottomNavigationView)

        val myButton = findViewById<Button>(R.id.startButton)
        myButton.setOnClickListener {
            // Вызывается при нажатии кнопки
            onMyButtonClick()
        }
    }
    private fun onMyButtonClick() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}