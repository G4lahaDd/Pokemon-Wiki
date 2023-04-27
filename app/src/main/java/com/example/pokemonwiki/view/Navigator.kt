package com.example.pokemonwiki.view

import com.example.pokemonwiki.model.PokemonInfo

/**
 * Use to navigate between fragments
 */
interface Navigator {
    fun showPokemonInfo(id : Long)
    fun back()
}

object NavigatorInstance{
    lateinit var navigator: Navigator
}