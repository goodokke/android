package com.example.test

import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class WordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word)

        val t1 = findViewById(R.id.textView12) as TextView
        t1.text = Html.fromHtml("<string>The laundry <b>room</b> is next to the utility <b>room</b>.</string>")

        val t2 = findViewById(R.id.textView15) as TextView
        t2.text = Html.fromHtml("<string><b>Перевод</b> вышеуказанного предложения, <b>такой</b> чтобы сохранить смысл</string>")
    }
}