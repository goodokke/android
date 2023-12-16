package com.example.test

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationHelper(private val context: Context, private val bottomNavigationView: BottomNavigationView) {

    init {
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    startActivity(MainActivity::class.java)

                    true
                }
                R.id.star -> {
                    menuItem.setIcon(R.drawable.ic_round_star);
                    startActivity(FavoritesActivity::class.java)
                    true
                }
                R.id.book -> {
                    startActivity(BookActivity::class.java)
                    true
                }
                else -> false
            }
        }
    }


    private fun startActivity(cls: Class<out Activity>) {
        val intent = Intent(context, cls)
        context.startActivity(intent)

        (context as? Activity)?.finish()
    }
}
