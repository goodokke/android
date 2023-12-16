package com.example.test

import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.InputStream


class FavoritesActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var originalData: ArrayList<ItemList>
    private lateinit var actualData: ArrayList<ItemList>
    private var scrollPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        supportActionBar?.hide()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        BottomNavigationHelper(this, bottomNavigationView)

        val menu = bottomNavigationView.menu
        val item = menu.findItem(R.id.star)
        item.isChecked = true

        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        val inputStream = assets.open("words.csv")
        val words = readCsv(inputStream)
        originalData = ArrayList<ItemList>()
        val sortedByName = words.sortedBy { it.word }.filter {
           sharedPreferences.getBoolean(it.word, false)
        }
        var counter = 0
        var isFirst = true

        sortedByName.forEach {
                val currentWord = it.word
                try {
                    val lastWord = sortedByName[counter - 1].word
                    val nextWord = sortedByName[counter + 1].word
                    if (currentWord[0].toUpperCase() != lastWord[0].toUpperCase() && currentWord[0].toUpperCase() == nextWord[0].toUpperCase()) {
                        originalData.add(
                            ItemList(
                                currentWord[0].toString().toUpperCase(),
                                capitalize(currentWord)
                            )
                        )
                    } else {
                        originalData.add(ItemList(null, capitalize(currentWord)))
                    }
                } catch (e: Exception) {
                    if (isFirst) {
                        originalData.add(
                            ItemList(
                                currentWord[0].toString().toUpperCase(),
                                capitalize(currentWord)
                            )
                        )
                        isFirst = false
                    } else {
                        val lastWord = sortedByName[counter - 1].word
                        if (currentWord[0].toUpperCase() != lastWord[0].toUpperCase())
                        {
                            originalData.add(
                                ItemList(
                                    currentWord[0].toString().toUpperCase(),
                                    capitalize(currentWord)
                                )
                            )
                        }
                        else{
                            originalData.add(ItemList(null, capitalize(currentWord)))
                        }
                    }
                }

            counter++
        }
        listView = findViewById<View>(R.id.list_words) as ListView
        listView.adapter = MyAdapter(this, originalData)
        actualData = originalData

        listView.setOnItemClickListener { parent, view, position, id ->
            // Обработка нажатия на элемент списка
            val selectedItem = actualData[position]

            val intent = Intent(this, WordActivity::class.java)
            intent.putExtra("word", selectedItem.subHeader)
            startActivity(intent)
        }
        val searchView = findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Вызывается при нажатии кнопки "Enter" или отправке запроса
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Вызывается при каждом изменении текста в поле поиска
                filterData(newText)
                return true
            }
        })

        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt("scrollPosition")
            println("FFFFFFFFFFFFFFFF")
        }
        listView.post {
            listView.setSelection(scrollPosition)
        }
    }

    private fun filterData(query: String?) {
        if (query == null || query == ""){
            listView.adapter = MyAdapter(this, originalData);
        }
        else {
            val filteredData = originalData.filter {
                it.subHeader?.contains(query.orEmpty(), ignoreCase = true) ?: true
            }
            val arrayListResult = ArrayList(filteredData)
            actualData = arrayListResult

            listView.adapter = MyAdapter(this, arrayListResult);
        }
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

    fun capitalize(str: String): String? {
        return str.capitalize()
    }
}