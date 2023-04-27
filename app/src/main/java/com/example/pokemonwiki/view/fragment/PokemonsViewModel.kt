package com.example.pokemonwiki.view.fragment

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokemonwiki.model.Pokemon
import com.example.pokemonwiki.model.service.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Provider

class PokemonsViewModel(
    private val pokemonRepository : PokemonRepository
) : ViewModel() {

    val isNetworkEnabled : Flow<Boolean> = pokemonRepository.isNetworkEnabled();
    val pokemonsFlow : Flow<PagingData<Pokemon>>

    init{
        pokemonsFlow = pokemonRepository.getPagedPokemons().cachedIn(viewModelScope)
    }

    fun trySetOnlineMode(){
        pokemonRepository.tryEnableNetwork()
    }
    fun setOfflineMode(){
        pokemonRepository.switchToOffline()
    }

}