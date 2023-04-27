package com.example.pokemonwiki.view.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pokemonwiki.model.PokemonInfo
import com.example.pokemonwiki.model.service.PokemonRepository
import com.example.pokemonwiki.view.NavigatorInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AboutPokemonViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _pokemonInfo = MutableLiveData<PokemonInfo>()
    val pokemonInfo: LiveData<PokemonInfo> = _pokemonInfo

    fun loadPokemonInfo(id: Long) {
        var pokemon: PokemonInfo? = null

        viewModelScope.launch {
            // when an application attempts to perform a networking operation on its main thread, throws NetworkOnMainThreadException
            viewModelScope.launch(Dispatchers.IO){
                pokemon = pokemonRepository.getPokemonById(id)
            }.join()

            if (pokemon != null) {
                _pokemonInfo.value = pokemon!!
            } else {
                NavigatorInstance.navigator.back()
            }
        }

    }

}