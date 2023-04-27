package com.example.pokemonwiki.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonwiki.R
import com.example.pokemonwiki.databinding.ItemPokemonLayoutBinding
import com.example.pokemonwiki.model.Pokemon
import com.example.pokemonwiki.model.PokemonInfo

interface PokemonListener {
    fun onSelectPokemon(id: Long)
}

class PokemonsAdapter(
    private val context: Context,
    private val listener: PokemonListener
) : PagingDataAdapter<Pokemon, PokemonsAdapter.PokemonViewHolder>(PokemonDiffItemCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPokemonLayoutBinding.inflate(inflater, parent, false)

        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position) ?: return
        holder.itemView.setOnClickListener { listener.onSelectPokemon(pokemon.id) }
        with(holder.binding) {
            holder.itemView.tag = pokemon
            nameText.text = pokemon.name;
        }
    }


    class PokemonViewHolder(val binding: ItemPokemonLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private object PokemonDiffItemCallback : DiffUtil.ItemCallback<Pokemon>() {

        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }
    }
}