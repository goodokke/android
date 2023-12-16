package com.example.test

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        // Инициализация SharedPreferences внутри метода onCreate
        sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        //Убрать очистку кэша
        clearSharedPreferences()
        // Устанавливаем таймер для перехода на другую Activity через 5 секунд (5000 миллисекунд)
        val delayMillis = 2000L
        // Запускаем таймер
        findViewById<View>(android.R.id.content).postDelayed({
            val isFirstLaunch = sharedPreferences.getBoolean("is_first_launch", true)

            if (isFirstLaunch) {
                startActivity(Intent(this, StartActivity::class.java))

                sharedPreferences.edit().putBoolean("is_first_launch", false).apply()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish() // Если вы хотите закрыть текущую Activity после перехода
        }, delayMillis)
    }

    private fun clearSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        Toast.makeText(this, "SharedPreferences очищены", Toast.LENGTH_SHORT).show()
    }
}