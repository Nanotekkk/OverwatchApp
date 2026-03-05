package com.example.overwatchapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HeroAdapter(
    private val items: MutableList<Hero>,
    private val repo: FavoritesRepository
) : RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

    inner class HeroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.itemHeroImage)
        val name = view.findViewById<TextView>(R.id.itemHeroName)
        val fav = view.findViewById<ImageView>(R.id.itemHeroFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hero, parent, false)
        return HeroViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = items[position]
        holder.name.text = hero.name
        holder.image.setImageResource(hero.imageRes)
        holder.fav.setImageResource(
            if (repo.isFavorite(hero.id)) R.drawable.ic_star_filled else R.drawable.ic_star_outline
        )

        holder.fav.setOnClickListener {
            repo.toggleFavorite(hero.id)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = items.size
}
