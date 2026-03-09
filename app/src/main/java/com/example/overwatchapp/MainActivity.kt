package com.example.overwatchapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_FEATURED_HERO = "featured_hero"
        private const val KEY_FEATURED_DATE = "featured_date"
        private const val KEY_FEATURED_HERO_ID = "featured_hero_id"
    }

    private var currentHeroId: String? = null
    private val activityJob: Job = SupervisorJob()
    private val activityScope = CoroutineScope(Dispatchers.Main + activityJob)
    private var heroes: List<Hero> = emptyList()
    private val featuredPrefs by lazy { getSharedPreferences(PREFS_FEATURED_HERO, MODE_PRIVATE) }

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

        currentHeroId = savedInstanceState?.getString("currentHeroId")
        heroName.text = "Chargement..."
        heroImage.setImageResource(R.drawable.ic_hero_placeholder)

        activityScope.launch {
            heroes = DataSource.getHeroes()
            val hero = getFeaturedHeroForToday(heroes)

            hero?.let {
                currentHeroId = it.id
                heroName.text = it.name
                heroImage.load(it.imageUrl) {
                    placeholder(it.imageRes)
                    error(it.imageRes)
                }
            } ?: run {
                heroName.text = "Aucun héros"
                heroImage.setImageResource(R.drawable.ic_hero_placeholder)
            }
        }

        toolbarTitle.setOnClickListener {
            val featuredHero = getFeaturedHeroForToday(heroes)
            featuredHero?.let {
                currentHeroId = it.id
                heroName.text = it.name
                heroImage.load(it.imageUrl) {
                    placeholder(it.imageRes)
                    error(it.imageRes)
                }
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

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }

    private fun getFeaturedHeroForToday(items: List<Hero>): Hero? {
        if (items.isEmpty()) return null

        val today = getTodayKey()
        val savedDate = featuredPrefs.getString(KEY_FEATURED_DATE, null)
        val savedHeroId = featuredPrefs.getString(KEY_FEATURED_HERO_ID, null)

        if (savedDate == today && !savedHeroId.isNullOrBlank()) {
            items.firstOrNull { it.id == savedHeroId }?.let { return it }
        }

        // Deterministic pick for the day, then persisted for full-day stability.
        val nonNegativeHash = today.hashCode().toLong() and 0x7fffffff
        val index = (nonNegativeHash % items.size).toInt()
        val featuredHero = items[index]

        featuredPrefs.edit()
            .putString(KEY_FEATURED_DATE, today)
            .putString(KEY_FEATURED_HERO_ID, featuredHero.id)
            .apply()

        return featuredHero
    }

    private fun getTodayKey(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }
}