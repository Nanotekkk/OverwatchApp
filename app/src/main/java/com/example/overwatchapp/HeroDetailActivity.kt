package com.example.overwatchapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HeroDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_HERO_ID = "extra_hero_id"
    }

    private val repository = OverwatchRepository()
    private val activityJob: Job = SupervisorJob()
    private val activityScope = CoroutineScope(Dispatchers.Main + activityJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_detail)

        val toolbarTitle = findViewById<TextView>(R.id.toolbarTitle)
        val detailName = findViewById<TextView>(R.id.detailHeroName)
        val detailImage = findViewById<ImageView>(R.id.detailHeroImage)
        val detailRole = findViewById<TextView>(R.id.detailHeroRole)
        val detailStats = findViewById<TextView>(R.id.detailHeroStats)
        val detailLocation = findViewById<TextView>(R.id.detailHeroLocation)
        val detailDescription = findViewById<TextView>(R.id.detailHeroDescription)

        toolbarTitle.setOnClickListener { finish() }

        val heroId = intent.getStringExtra(EXTRA_HERO_ID)
        if (heroId.isNullOrBlank()) {
            detailName.text = "Héros introuvable"
            return
        }

        detailName.text = "Chargement..."
        detailImage.setImageResource(R.drawable.ic_hero_placeholder)

        activityScope.launch {
            runCatching { repository.getHeroDetails(heroId) }
                .onSuccess { detail ->
                    toolbarTitle.text = detail.name
                    detailName.text = detail.name
                    detailImage.load(detail.portraitUrl) {
                        placeholder(R.drawable.ic_hero_placeholder)
                        error(R.drawable.ic_hero_placeholder)
                    }

                    detailRole.text = "Rôle: ${detail.role ?: "Inconnu"}"
                    detailStats.text = "HP ${detail.totalHp ?: 0} (Santé ${detail.health ?: 0} | Armure ${detail.armor ?: 0} | Boucliers ${detail.shields ?: 0})"

                    val ageText = detail.age?.toString() ?: "Inconnu"
                    val birthdayText = detail.birthday ?: "Inconnu"
                    val locationText = detail.location ?: "Inconnu"
                    detailLocation.text = "Âge: $ageText • Anniversaire: $birthdayText\nLocalisation: $locationText"

                    detailDescription.text = detail.description ?: "Aucune description disponible."
                }
                .onFailure {
                    detailName.text = "Erreur de chargement"
                    detailDescription.text = "Impossible de récupérer les détails du héros."
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }
}
