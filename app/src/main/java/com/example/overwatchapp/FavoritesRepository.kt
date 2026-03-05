package com.example.overwatchapp

import android.content.Context

class FavoritesRepository(context: Context) {
    private val prefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    private val KEY = "favorite_ids"

    fun getFavorites(): MutableSet<String> {
        return prefs.getStringSet(KEY, emptySet())?.toMutableSet() ?: mutableSetOf()
    }

    fun isFavorite(id: String): Boolean {
        return getFavorites().contains(id)
    }

    fun addFavorite(id: String) {
        val set = getFavorites()
        set.add(id)
        prefs.edit().putStringSet(KEY, set).apply()
    }

    fun removeFavorite(id: String) {
        val set = getFavorites()
        set.remove(id)
        prefs.edit().putStringSet(KEY, set).apply()
    }

    fun toggleFavorite(id: String) {
        if (isFavorite(id)) removeFavorite(id) else addFavorite(id)
    }
}
