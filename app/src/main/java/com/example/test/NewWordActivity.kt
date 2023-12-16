package com.example.test

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.InputStream
import java.util.Locale

class NewWordActivity : AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var new_words: ArrayList<Word>
    private var selectIndex = 0
    private var box = 0
    private var tts: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)
        supportActionBar?.hide()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        BottomNavigationHelper(this, bottomNavigationView)

        sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        tts = TextToSpeech(this, this)

        new_words = ArrayList<Word>()
        box = sharedPreferences.getInt("box", 2)

        val inputStream = assets.open("words.csv")
        val words = readCsv(inputStream)
        var counter = 0
        val firstIndex = box*5;
        words.forEach {
            // count 0-1000 box 0 0-4 1 5-9 2 10-14 3 15-19
            if(counter >= firstIndex && counter < firstIndex+5){
                new_words.add(it)
            }
            counter++
        }

        val textViewWord = findViewById<TextView>(R.id.textView4)
        val textViewTrans = findViewById<TextView>(R.id.textView10)
        val textViewRus = findViewById<TextView>(R.id.textView11)


        val word = new_words[selectIndex];

        textViewWord.setText(capitalize(word.word))

        textViewTrans.setText(word.trans)

        textViewRus.setText(capitalize(word.rus))

        val buttonNext = findViewById<Button>(R.id.button2)
        val buttonBack = findViewById<Button>(R.id.button1)

        buttonNext.setText("Далее")
        buttonNext.setOnClickListener {
            //ЗАГЛУШКА ПОКА НЕТ ПРАКТИКИ
            if(buttonNext.text == "Завершить"){
                sharedPreferences.edit().putInt("box", box+1).apply()
                sharedPreferences.edit().putString("lastWord", capitalize(new_words[selectIndex].word)).apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                selectIndex++
                if (selectIndex == 4) {
                    buttonNext.setText("Завершить")
                }


                val word = new_words[selectIndex];

                textViewWord.setText(capitalize(word.word))

                textViewTrans.setText(word.trans)

                textViewRus.setText(capitalize(word.rus))
            }
        }

        buttonBack.setOnClickListener {
            selectIndex--
            if(selectIndex < 0){
                finish()
            }
            else{
                buttonNext.setText("Далее")
                val word = new_words[selectIndex];

                textViewWord.setText(capitalize(word.word))

                textViewTrans.setText(word.trans)

                textViewRus.setText(capitalize(word.rus))
            }
        }

        val imageView = findViewById<ImageView>(R.id.imageView6)
        imageView.setOnClickListener{
            val text = new_words[selectIndex].word
            tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
        }



        val t1 = findViewById(R.id.textView12) as TextView
        t1.text = Html.fromHtml("<string>The laundry <b>room</b> is next to the utility <b>room</b>.</string>")

        val t2 = findViewById(R.id.textView15) as TextView
        t2.text = Html.fromHtml("<string><b>Перевод</b> вышеуказанного предложения, <b>такой</b> чтобы сохранить смысл</string>")
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
}