package com.example.overwatchapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HeroesActivity : AppCompatActivity() {
    private lateinit var repo: FavoritesRepository
    private lateinit var recycler: RecyclerView
    private val activityJob: Job = SupervisorJob()
    private val activityScope = CoroutineScope(Dispatchers.Main + activityJob)

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

        recycler = findViewById(R.id.recyclerHeroes)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        activityScope.launch {
            val heroes = DataSource.getHeroes()
            val adapter = HeroAdapter(heroes.toMutableList(), repo) { hero ->
                val intent = Intent(this@HeroesActivity, HeroDetailActivity::class.java)
                intent.putExtra(HeroDetailActivity.EXTRA_HERO_ID, hero.id)
                startActivity(intent)
            }
            recycler.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }
}
