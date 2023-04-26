package com.example.pokemonwiki.di

import android.content.Context
import com.example.pokemonwiki.model.service.PokemonRepository
import com.example.pokemonwiki.view.HomeActivity
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    val pokemonRepository : PokemonRepository

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: HomeActivity)
}