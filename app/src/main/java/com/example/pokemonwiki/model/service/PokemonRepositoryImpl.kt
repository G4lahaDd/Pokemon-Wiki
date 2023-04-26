package com.example.pokemonwiki.model.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.util.Log
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.pokemonwiki.model.Pokemon
import com.example.pokemonwiki.model.PokemonInfo
import com.example.pokemonwiki.model.remote.PokemonService
import com.example.pokemonwiki.model.storage.PokemonDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton


class PokemonRepositoryImpl @Inject constructor(
    val pokemonService: PokemonService,
    val pokemonDao: PokemonDao,
    val applicationContext: Context
) : PokemonRepository{

    private val isConnected = MutableStateFlow(true)

    init {
        isConnected.value = isOnline()
    }

    override fun isNetworkEnabled(): Flow<Boolean> = isConnected

    override fun tryEnableNetwork() {
        isConnected.value = isOnline();
    }

    /**
     *
     * @throws HttpException Error while getting pokemons
     */
    override suspend fun getPagedPokemons(page: Int, size: Int): List<Pokemon> {
        val offset = page * PokemonRepository.DEFAULT_PAGE_SIZE
        if(isConnected.value){
            val response = pokemonService.getPokemons(offset = offset, limit = size)
            if (response.isSuccessful)
                return response.body()?.pokemons?.map { it.toPokemon() }?: emptyList()
            else throw HttpException(response)
        }else{
            return pokemonDao.getPokemons(offset = offset, limit = size).map{it.toPokemon()}
        }
    }

    override suspend fun getPokemonById(id: Int): PokemonInfo? {
        var pokemon : PokemonInfo? = null

        pokemon = pokemonDao.getPokemonById(id)?.toPokemonInfo();

        if(isConnected.value){
            val response = pokemonService.getPokemonById(id)
            if(response.isSuccessful && response.body() != null){
                if(pokemon != null){
                    pokemon = response.body()?.toPokemonInfo()
                    if(pokemon != null ) pokemonDao.updatePokemon(pokemon.toPokemonInfoDbo())}
                else{
                    pokemon = response.body()?.toPokemonInfo()
                    if(pokemon != null ) pokemonDao.addPokemon(pokemon.toPokemonInfoDbo())
                }
            }
        }
        return pokemon
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        Log.i("Internet", "Offline mode")
        return false
    }
}