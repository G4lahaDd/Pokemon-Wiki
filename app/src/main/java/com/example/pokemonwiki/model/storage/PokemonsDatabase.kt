package com.example.pokemonwiki.model.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokemonwiki.model.storage.model.PokemonInfoDbo

@Database(version = 1,
    entities = [PokemonInfoDbo::class])
@TypeConverters(Converters::class)
abstract class PokemonsDatabase : RoomDatabase()  {
    abstract fun getPokemonsDao() : PokemonDao
}