package com.example.test

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.InputStream
import java.util.Locale


class WordActivity : AppCompatActivity(),TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var isFavorite: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word)
        supportActionBar?.hide()
        tts = TextToSpeech(this, this)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        BottomNavigationHelper(this, bottomNavigationView)

        val menu = bottomNavigationView.menu
        val item = menu.findItem(R.id.book)
        item.isChecked = true

        val receivedIntent = intent
        val str = receivedIntent.getStringExtra("word")

        val inputStream = assets.open("words.csv")
        val words = readCsv(inputStream)
        val word = words.find { capitalize(it.word) == str.toString() }

        println(str)
        println(word)

        val textViewWord = findViewById<TextView>(R.id.textView4)
        val textViewTrans = findViewById<TextView>(R.id.textView10)
        val textViewRus = findViewById<TextView>(R.id.textView11)
        if (word != null) {
            textViewWord.setText(capitalize(word.word))
        }
        textViewTrans.setText(word?.trans)
        if (word != null) {
            textViewRus.setText(capitalize(word.rus))
        }

        val t1 = findViewById(R.id.textView12) as TextView
        t1.text = Html.fromHtml("<string>The laundry <b>room</b> is next to the utility <b>room</b>.</string>")

        val t2 = findViewById(R.id.textView15) as TextView
        t2.text = Html.fromHtml("<string><b>Перевод</b> вышеуказанного предложения, <b>такой</b> чтобы сохранить смысл</string>")

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            finish()
        }

        val imageView = findViewById<ImageView>(R.id.imageView6)
        imageView.setOnClickListener{
            if (word != null) {
                val text = word.word
                tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
            }
        }

        // Инициализация SharedPreferences внутри метода onCreate
        sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        val imageViewStar = findViewById<ImageView>(R.id.imageView5)

        isFavorite = sharedPreferences.getBoolean(word?.word, false)
        if(isFavorite){
            imageViewStar.setColorFilter(ContextCompat.getColor(this, R.color.yellow))
        }
        else
        {
            imageViewStar.setColorFilter(ContextCompat.getColor(this, R.color.favorite))
        }

        imageViewStar.setOnClickListener {
            if(isFavorite){
                isFavorite = false
                imageViewStar.setColorFilter(ContextCompat.getColor(this, R.color.favorite))
            }
            else{
                isFavorite = true
                imageViewStar.setColorFilter(ContextCompat.getColor(this, R.color.yellow))
            }
            sharedPreferences.edit().putBoolean(word?.word, isFavorite).apply()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.ENGLISH)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language not supported!")
            }
        }
    }
    fun capitalize(str: String): String? {
        return str.capitalize()
    }
    fun readCsv(inputStream: InputStream): List<Word> {
        val reader = inputStream.bufferedReader()
        val header = reader.readLine()
        return reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val (word, trans, rus) = it.split(',', ignoreCase = false, limit = 3)
                Word(word.trim(), trans.trim(), rus.trim())
            }.toList()
    }

    public override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}