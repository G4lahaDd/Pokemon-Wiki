package com.example.pokemonwiki.model.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pokemonwiki.model.storage.model.PokemonDbo
import com.example.pokemonwiki.model.storage.model.PokemonInfoDbo

@Dao
interface PokemonDao {
    @Query("SELECT id, name FROM pokemons " +
            "LIMIT :limit OFFSET :offset")
    suspend fun getPokemons(limit: Int, offset: Int): List<PokemonDbo>

    @Query("SELECT * FROM pokemons " +
            "WHERE id = :id")
    suspend fun getPokemonById(id: Long) : PokemonInfoDbo?

    @Insert
    suspend fun addPokemon(pokemon : PokemonInfoDbo)

    @Update
    suspend fun updatePokemon(pokemon : PokemonInfoDbo)
}