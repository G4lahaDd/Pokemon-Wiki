package com.example.pokemonwiki.di

import android.content.Context
import androidx.room.Room
import com.example.pokemonwiki.model.remote.PokemonService
import com.example.pokemonwiki.model.service.PokemonRepository
import com.example.pokemonwiki.model.service.paging.PokemonRepositoryImpl
import com.example.pokemonwiki.model.storage.PokemonDao
import com.example.pokemonwiki.model.storage.PokemonsDatabase
import com.example.pokemonwiki.view.fragment.AboutPokemonViewModel
import com.example.pokemonwiki.view.fragment.PokemonsViewModel
import com.example.pokemonwiki.view.fragment.PokemonsViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [RemoteModule::class, StorageModule::class])
abstract class AppModule {
    @Binds
    abstract fun bindPokemonRepository(pokemonRepositoryImpl: PokemonRepositoryImpl): PokemonRepository

    companion object{
        @Provides
        fun provideAboutPokemonViewModel(factory : PokemonsViewModelFactory) : AboutPokemonViewModel{
            return factory.create(AboutPokemonViewModel::class.java)
        }

        @Provides
        fun providePokemonsViewModel(factory : PokemonsViewModelFactory) : PokemonsViewModel{
            return factory.create(PokemonsViewModel::class.java)
        }
    }
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