package com.example.overwatchapp.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

data class OverwatchHeroDto(
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("portrait") val portrait: String?
)

data class HitpointsDto(
    @SerializedName("health") val health: Int?,
    @SerializedName("armor") val armor: Int?,
    @SerializedName("shields") val shields: Int?,
    @SerializedName("total") val total: Int?
)

data class OverwatchHeroDetailDto(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("portrait") val portrait: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("age") val age: Int?,
    @SerializedName("hitpoints") val hitpoints: HitpointsDto?
)

interface OverwatchApiService {
    @GET("heroes")
    suspend fun getHeroes(): List<OverwatchHeroDto>

    @GET("heroes/{key}")
    suspend fun getHeroDetails(@retrofit2.http.Path("key") key: String): OverwatchHeroDetailDto
}
