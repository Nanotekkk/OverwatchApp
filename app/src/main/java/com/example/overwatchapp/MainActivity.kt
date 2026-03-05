package com.example.overwatchapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private var currentHeroId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 🔥 Récupération du token Firebase
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Token error", task.exception)
                    return@addOnCompleteListener
                }

                val token = task.result
                Log.d("FCM_TOKEN", token)
            }

        val toolbarTitle = findViewById<TextView>(R.id.toolbarTitle)
        val heroImage = findViewById<ImageView>(R.id.heroImage)
        val heroName = findViewById<TextView>(R.id.heroName)
        val openListButton = findViewById<Button>(R.id.openHeroesButton)

        val heroes = DataSource.getHeroes()

        // Restore or pick a random hero
        currentHeroId = savedInstanceState?.getString("currentHeroId")
        val hero = if (currentHeroId != null) {
            heroes.firstOrNull { it.id == currentHeroId } ?: heroes.firstOrNull()
        } else {
            heroes.shuffled().firstOrNull()
        }

        hero?.let {
            currentHeroId = it.id
            heroName.text = it.name
            heroImage.setImageResource(it.imageRes)
        } ?: run {
            heroName.text = "Aucun héros"
            heroImage.setImageResource(R.drawable.ic_hero_placeholder)
        }

        toolbarTitle.setOnClickListener {
            val newHero = heroes.shuffled().firstOrNull()
            newHero?.let {
                currentHeroId = it.id
                heroName.text = it.name
                heroImage.setImageResource(it.imageRes)
            }
        }

        openListButton.setOnClickListener {
            val intent = Intent(this, HeroesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentHeroId", currentHeroId)
    }
}