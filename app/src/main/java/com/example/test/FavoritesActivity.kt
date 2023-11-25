package com.example.test

import android.content.ClipData.Item
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class FavoritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

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


//        val arrayAdapter: ArrayAdapter<*>
//        val array_list = ArrayList<String>()
//        var mListView = findViewById<ListView>(R.id.list_words)
//        var set =  HashSet<String>()
//        set.addAll(listOf("Back", "Black"))
//        array_list.add(0, "B");
//        array_list.addAll(set);
//        val words = arrayOf(
//            "Virat Kohli", "Rohit Sharma", "Steve Smith",
//            "Kane Williamson", "Ross Taylor"
//        )
//
//        // access the listView from xml file
//
//        array_list.trimToSize()
//        arrayAdapter = ArrayAdapter(this,
//            android.R.layout.simple_list_item_1, array_list)
//        mListView.adapter = arrayAdapter
    }
}