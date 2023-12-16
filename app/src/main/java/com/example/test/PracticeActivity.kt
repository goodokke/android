package com.example.test

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.InputStream

class PracticeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var new_words: ArrayList<Word>
    private var isFirst = true
    private var english = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)
        supportActionBar?.hide()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        BottomNavigationHelper(this, bottomNavigationView)

        sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        new_words = ArrayList<Word>()
        var box = sharedPreferences.getInt("box", 2)

        val inputStream = assets.open("words.csv")
        val words = readCsv(inputStream)
        var counter = 0
        val firstIndex = box*5;
        words.forEach {
            // count 0-1000 box 0 0-4 1 5-9 2 10-14 3 15-19
            if(counter < firstIndex){
                new_words.add(it)
            }
            counter++
        }

        var startButton = findViewById<Button>(R.id.startButton)

        startButton.setBackgroundResource(R.drawable.roundedbuttongrey)
        startButton.isEnabled = false

        startButton.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
        }

        var shuffledWords = randomButtons(new_words)

        val buttons = arrayOf(
            findViewById<Button>(R.id.button1),
            findViewById<Button>(R.id.button4),
            findViewById<Button>(R.id.button5),
            findViewById<Button>(R.id.button6),
            findViewById<Button>(R.id.button7),
            findViewById<Button>(R.id.button8)
        )

        for (button in buttons) {
            button.setOnClickListener {
                if (isFirst){
                    button.setBackgroundResource(R.drawable.roundedbutton)
                    isFirst = false
                    english = button.text.toString()
                    button.isEnabled = false
                }
            }
        }

        val russianButtons = arrayOf(
            findViewById<Button>(R.id.button3),
            findViewById<Button>(R.id.button10),
            findViewById<Button>(R.id.button11),
            findViewById<Button>(R.id.button12),
            findViewById<Button>(R.id.button13),
            findViewById<Button>(R.id.button9)
        )

        for (button in russianButtons) {
            button.setOnClickListener {
                if (!isFirst){
                    var word = shuffledWords.find { it.rus == button.text.toString() }
                    if (word != null) {
                        if (word.word == english) {
                            button.setBackgroundResource(R.drawable.roundedbutton)
                        } else {
                            button.setBackgroundResource(R.drawable.roundedbuttonred)
                            var buttonEn1 = findButtonByText(word.word)
                            buttonEn1.setBackgroundResource(R.drawable.roundedbuttonred)
                            buttonEn1.isEnabled = false
                            var buttonEn2 = findButtonByText(english)
                            buttonEn2.setBackgroundResource(R.drawable.roundedbuttonred)
                            buttonEn2.isEnabled = false
                            var wordEn = shuffledWords.find { it.word == english }
                            if(wordEn != null){
                                var buttonRus = findButtonByText(wordEn.rus)
                                buttonRus.setBackgroundResource(R.drawable.roundedbuttonred)
                                buttonRus.isEnabled = false
                            }
                        }
                        button.isEnabled = false
                        isFirst = true
                    }
                    if(checkButtons()){
                        startButton.setBackgroundResource(R.drawable.roundedbutton)
                        startButton.isEnabled = true
                    }
                }
            }
        }
    }

    fun checkButtons():Boolean{
        val buttons = arrayOf(
            findViewById<Button>(R.id.button1),
            findViewById<Button>(R.id.button4),
            findViewById<Button>(R.id.button5),
            findViewById<Button>(R.id.button6),
            findViewById<Button>(R.id.button7),
            findViewById<Button>(R.id.button8),
            findViewById<Button>(R.id.button3),
            findViewById<Button>(R.id.button10),
            findViewById<Button>(R.id.button11),
            findViewById<Button>(R.id.button12),
            findViewById<Button>(R.id.button13),
            findViewById<Button>(R.id.button9)
        )

        for (button in buttons) {
            if (button.isEnabled) {
                return false
            }
        }

        return true
    }

    fun findButtonByText(text: String): Button {
        val buttons = arrayOf(
            findViewById<Button>(R.id.button1),
            findViewById<Button>(R.id.button4),
            findViewById<Button>(R.id.button5),
            findViewById<Button>(R.id.button6),
            findViewById<Button>(R.id.button7),
            findViewById<Button>(R.id.button8),
            findViewById<Button>(R.id.button3),
            findViewById<Button>(R.id.button10),
            findViewById<Button>(R.id.button11),
            findViewById<Button>(R.id.button12),
            findViewById<Button>(R.id.button13),
            findViewById<Button>(R.id.button9)
        )

        for (button in buttons) {
            if (button.text.toString() == text) {
                return button
            }
        }

        return buttons[0]
    }

    fun randomButtons(words: ArrayList<Word>):List<Word>{
        val buttons = arrayOf(
            findViewById<Button>(R.id.button1),
            findViewById<Button>(R.id.button4),
            findViewById<Button>(R.id.button5),
            findViewById<Button>(R.id.button6),
            findViewById<Button>(R.id.button7),
            findViewById<Button>(R.id.button8)
        )

        val russianButtons = arrayOf(
            findViewById<Button>(R.id.button3),
            findViewById<Button>(R.id.button10),
            findViewById<Button>(R.id.button11),
            findViewById<Button>(R.id.button12),
            findViewById<Button>(R.id.button13),
            findViewById<Button>(R.id.button9)
        )

        val shuffledWords = words.shuffled().subList(0, 6)
        var shuffledWordsRus = shuffledWords.shuffled()

        for (i in buttons.indices) {
            buttons[i].text = shuffledWords[i].word
            russianButtons[i].text = shuffledWordsRus[i].rus
        }

        return shuffledWords;
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