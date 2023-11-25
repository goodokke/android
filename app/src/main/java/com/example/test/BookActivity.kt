package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView

class BookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        val data = ArrayList<ItemList>()

        data.add(ItemList("B", "Back"))
        data.add(ItemList(null, "Black"))
        data.add(ItemList("D", "Dog"))
        data.add(ItemList(null, "Delphin"))
        data.add(ItemList("H", "Hello"))
        data.add(ItemList("R", "Red"))
        data.add(ItemList("T", "Tea"))
        val lv = findViewById<View>(R.id.list_words) as ListView
        lv.adapter = MyAdapter(this, data)
    }
}