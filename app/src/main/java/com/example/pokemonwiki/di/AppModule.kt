package com.example.pokemonwiki.di

import android.content.Context
import androidx.room.Room
import com.example.pokemonwiki.model.remote.PokemonService
import com.example.pokemonwiki.model.service.PokemonRepository
import com.example.pokemonwiki.model.service.PokemonRepositoryImpl
import com.example.pokemonwiki.model.storage.PokemonDao
import com.example.pokemonwiki.model.storage.PokemonsDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [RemoteModule::class, StorageModule::class])
abstract class AppModule {
    @Binds
    abstract fun bindPokemonRepository(pokemonRepositoryImpl: PokemonRepositoryImpl): PokemonRepository
}

@Module
class RemoteModule {

    @Provides
    fun providesPokemonService(): PokemonService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(PokemonService::class.java)
    }
}

@Module
class StorageModule {

    @Provides
    fun providePokemonDao(applicationContext: Context): PokemonDao {
        return Room.databaseBuilder(applicationContext, PokemonsDatabase::class.java, "pokemons.db")
            .build().getPokemonsDao()
    }

}