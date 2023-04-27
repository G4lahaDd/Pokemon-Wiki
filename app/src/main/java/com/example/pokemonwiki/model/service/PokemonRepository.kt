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
     * try to enable network
     */
    fun switchToOffline()

    /**
     * Get the paging list of pokemons.
     */
    fun getPagedPokemons(): Flow<PagingData<Pokemon>>

    /**
     * Get the pokemon by id.
     */
    suspend fun getPokemonById(id : Long) : PokemonInfo?

    companion object{
        const val DEFAULT_PAGE_SIZE : Int = 20
    }
}