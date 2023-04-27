package com.example.pokemonwiki.view.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokemonwiki.di.AppComponent
import com.example.pokemonwiki.model.service.PokemonRepository
import com.example.pokemonwiki.view.Navigator
import com.example.pokemonwiki.view.NavigatorInstance
import javax.inject.Inject
import javax.inject.Singleton

class PokemonsViewModelFactory @Inject constructor(private val repository: PokemonRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(PokemonsViewModel::class.java)) {
            return PokemonsViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(AboutPokemonViewModel::class.java)) {
            return AboutPokemonViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun Fragment.navigator() = NavigatorInstance.navigator