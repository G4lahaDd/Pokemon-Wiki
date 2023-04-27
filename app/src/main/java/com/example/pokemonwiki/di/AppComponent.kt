package com.example.pokemonwiki.di

import android.content.Context
import com.example.pokemonwiki.model.service.PokemonRepository
import com.example.pokemonwiki.view.HomeActivity
import com.example.pokemonwiki.view.fragment.AboutPokemonViewModel
import com.example.pokemonwiki.view.fragment.PokemonsViewModel
import com.example.pokemonwiki.view.fragment.PokemonsViewModelFactory
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    val pokemonsViewModel : PokemonsViewModel
    val aboutPokemonViewModel : AboutPokemonViewModel
    //val pokemonsViewModelFactory : PokemonsViewModelFactory

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: HomeActivity)
}