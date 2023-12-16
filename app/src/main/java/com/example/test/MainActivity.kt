package com.example.test

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.test.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        BottomNavigationHelper(this, bottomNavigationView)

        val menu = bottomNavigationView.menu
        val item = menu.findItem(R.id.home)
        item.isChecked = true

        var progressBar = findViewById<ProgressBar>(R.id.progressBar)
        var textViewCountWords = findViewById<TextView>(R.id.textView9)

        var textViewLastWord = findViewById<TextView>(R.id.textView8)
        var countFavorite = sharedPreferences.getInt("countFavorite", 0)
        var box = sharedPreferences.getInt("box", 2)
        var progressResult = (box * 5).toFloat() / 1000 * 100
        progressBar.progress = progressResult.toInt()
        var lastWord = sharedPreferences.getString("lastWord", "*")
        textViewCountWords.setText((box*5).toString().plus("/1000 слов"))
        textViewLastWord.setText("Последнее изученное слово: ".plus(lastWord))
        var newWordsButton = findViewById<Button>(R.id.startButton)
        newWordsButton.setOnClickListener {
            val intent = Intent(this, NewWordActivity::class.java)
            startActivity(intent)
        }

        var imageViewStart = findViewById<ImageView>(R.id.imageView)

        imageViewStart.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent)
        }

//        sharedPreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, s ->
//            if (s == "box") {
//                val newValue = sharedPreferences.getInt(s, 0)
//                var progressBar = findViewById<ProgressBar>(R.id.progressBar)
//                progressBar.progress = (newValue*5 / 1000)*100
//            }
//        }
    }
}