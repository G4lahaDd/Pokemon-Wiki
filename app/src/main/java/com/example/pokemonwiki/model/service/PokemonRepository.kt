package com.example.pokemonwiki.model.service

import androidx.paging.PagingData
import com.example.pokemonwiki.model.Pokemon
import com.example.pokemonwiki.model.PokemonInfo
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    /**
     * Whether network are enabled or not. The value is listened
     * in the HomeActivity.
     */
    fun isNetworkEnabled(): Flow<Boolean>

    /**
     * try to enable network
     */
    fun tryEnableNetwork()

    /**
     * Get the paging list of pokemons.
     */
    suspend fun getPagedPokemons(page: Int, size: Int): List<Pokemon>

    /**
     * Get the pokemon by id.
     */
    suspend fun getPokemonById(id : Int) : PokemonInfo?

    companion object{
        const val DEFAULT_PAGE_SIZE : Int = 20
    }
}