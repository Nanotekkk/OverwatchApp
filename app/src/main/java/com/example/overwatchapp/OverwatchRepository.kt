package com.example.overwatchapp

import com.example.overwatchapp.api.OverwatchApiClient
import com.example.overwatchapp.api.OverwatchHeroDetailDto
import com.example.overwatchapp.api.OverwatchApiService
import com.example.overwatchapp.api.OverwatchHeroDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class HeroDetails(
    val id: String,
    val name: String,
    val portraitUrl: String?,
    val role: String?,
    val description: String?,
    val location: String?,
    val birthday: String?,
    val age: Int?,
    val health: Int?,
    val armor: Int?,
    val shields: Int?,
    val totalHp: Int?
)

class OverwatchRepository(
    private val apiService: OverwatchApiService = OverwatchApiClient.service
) {
    suspend fun getHeroes(): List<Hero> = withContext(Dispatchers.IO) {
        apiService.getHeroes()
            .sortedBy { it.name }
            .map { it.toHero() }
    }

    suspend fun getHeroDetails(heroId: String): HeroDetails = withContext(Dispatchers.IO) {
        apiService.getHeroDetails(heroId).toHeroDetails(heroId)
    }
}

private fun OverwatchHeroDto.toHero(): Hero {
    return Hero(
        id = key,
        name = name,
        imageRes = R.drawable.ic_hero_placeholder,
        imageUrl = portrait
    )
}

private fun OverwatchHeroDetailDto.toHeroDetails(heroId: String): HeroDetails {
    return HeroDetails(
        id = heroId,
        name = name,
        portraitUrl = portrait,
        role = role,
        description = description,
        location = location,
        birthday = birthday,
        age = age,
        health = hitpoints?.health,
        armor = hitpoints?.armor,
        shields = hitpoints?.shields,
        totalHp = hitpoints?.total
    )
}
