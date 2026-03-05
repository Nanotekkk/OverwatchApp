package com.example.overwatchapp

object DataSource {
    fun getHeroes(): List<Hero> {
        return listOf(
            Hero("dva", "D.Va", R.drawable.ic_hero_placeholder),
            Hero("reinhardt", "Reinhardt", R.drawable.ic_hero_placeholder),
            Hero("tracer", "Tracer", R.drawable.ic_hero_placeholder),
            Hero("mercy", "Mercy", R.drawable.ic_hero_placeholder),
            Hero("soldier76", "Soldier: 76", R.drawable.ic_hero_placeholder)
        )
    }
}
