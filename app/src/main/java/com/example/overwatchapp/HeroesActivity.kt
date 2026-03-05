package com.example.overwatchapp

import android.os.Bundle
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

        val recycler = findViewById<RecyclerView>(R.id.recyclerHeroes)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val heroes = DataSource.getHeroes()
        val adapter = HeroAdapter(heroes.toMutableList(), repo)
        recycler.adapter = adapter
    }
}
