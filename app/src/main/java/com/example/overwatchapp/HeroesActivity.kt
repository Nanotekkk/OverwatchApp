package com.example.overwatchapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HeroesActivity : AppCompatActivity() {
    private lateinit var repo: FavoritesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heroes)

        repo = FavoritesRepository(this)

        val toolbarTitle = findViewById<TextView>(R.id.toolbarTitle)
        toolbarTitle.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        val recycler = findViewById<RecyclerView>(R.id.recyclerHeroes)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val heroes = DataSource.getHeroes()
        val adapter = HeroAdapter(heroes.toMutableList(), repo)
        recycler.adapter = adapter
    }
}
