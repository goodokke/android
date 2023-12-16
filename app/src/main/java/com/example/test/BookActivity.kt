package com.example.test

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStream

class BookActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var originalData: ArrayList<ItemList>
    private lateinit var actualData: ArrayList<ItemList>
    private var scrollPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        supportActionBar?.hide()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        BottomNavigationHelper(this, bottomNavigationView)
        val menu = bottomNavigationView.menu
        val item = menu.findItem(R.id.book)
        item.isChecked = true

        val inputStream = assets.open("words.csv")
        val words = readCsv(inputStream)
        originalData = ArrayList<ItemList>()
        val sortedByName = words.sortedBy { it.word }
        var counter = 0
        var isFirst = true
        sortedByName.forEach {
            val currentWord = it.word
            try {
                val lastWord = sortedByName[counter - 1].word
                val nextWord = sortedByName[counter + 1].word
                if (currentWord[0].toUpperCase() != lastWord[0].toUpperCase() && currentWord[0].toUpperCase() == nextWord[0].toUpperCase()) {
                    originalData.add(ItemList(currentWord[0].toString().toUpperCase(), capitalize(currentWord)))
                } else {
                    originalData.add(ItemList(null, capitalize(currentWord)))
                }
            }
            catch(e: Exception){
                if(isFirst)
                {
                    originalData.add(ItemList(currentWord[0].toString().toUpperCase(), capitalize(currentWord)))
                    isFirst = false
                }
                else
                {
                    originalData.add(ItemList(null, capitalize(currentWord)))
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Сохранение положения скролла перед уничтожением активности
        println("AAAAAAAAAAAAA")
        outState.putInt("scrollPosition", listView.lastVisiblePosition)
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

    private fun readCSVFile() {
        val bufferReader = BufferedReader(assets.open("words.csv").reader())
        val csvParser = CSVParser.parse(
            bufferReader,
            CSVFormat.DEFAULT
        )
        val list= mutableListOf<Word>()
        csvParser.forEach {
            it?.let {
                val words=  Word(
                    word=it.get(0),
                    trans=it.get(1),
                    rus = it.get(2)
                )
                list.add(words)
            }
        }
        val data = ArrayList<ItemList>()
        data.add(ItemList("B", "Back"))
        list.forEach {
            data.add(ItemList(null, it.word))
        }

    }
}