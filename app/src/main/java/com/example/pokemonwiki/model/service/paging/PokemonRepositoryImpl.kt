package com.example.pokemonwiki.model.service.paging

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pokemonwiki.model.Pokemon
import com.example.pokemonwiki.model.PokemonInfo
import com.example.pokemonwiki.model.remote.PokemonService
import com.example.pokemonwiki.model.service.PokemonRepository
import com.example.pokemonwiki.model.service.PokemonRepository.Companion.DEFAULT_PAGE_SIZE
import com.example.pokemonwiki.model.service.toPokemon
import com.example.pokemonwiki.model.service.toPokemonInfo
import com.example.pokemonwiki.model.service.toPokemonInfoDbo
import com.example.pokemonwiki.model.storage.PokemonDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject


class PokemonRepositoryImpl @Inject constructor(
    private val pokemonService: PokemonService,
    private val pokemonDao: PokemonDao,
    private val applicationContext: Context
) : PokemonRepository {

    private val isConnected = MutableStateFlow(true)

    init {
        isConnected.value = isOnline()
    }

    override fun isNetworkEnabled(): Flow<Boolean> = isConnected

    override fun tryEnableNetwork() {
        isConnected.value = isOnline();
    }

    override fun switchToOffline() {
        isConnected.value = false;
    }

    /**
     *
     * @throws HttpException Error while getting pokemons
     */
    override fun getPagedPokemons(): Flow<PagingData<Pokemon>> {
       //create abstract data source for pager
        val source: PokemonsSource = { pageIndex, pageSize ->
            getPokemons(pageIndex, pageSize)
        }
        return Pager(
            config = PagingConfig(
                pageSize = PokemonRepository.DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PokemonPagingSource(
                    source,
                    PokemonRepository.DEFAULT_PAGE_SIZE
                )
            }
        ).flow
    }

    override suspend fun getPokemonById(id: Long): PokemonInfo? {
        var pokemon: PokemonInfo? = null

        // take pokemon from local storage, if exists
        pokemon = pokemonDao.getPokemonById(id)?.toPokemonInfo();

        if (isConnected.value) {
            //try to get pokemon from remote storage
            val response = pokemonService.getPokemonById(id)
            if (response.isSuccessful && response.body() != null) {
                //Update local storage, if response successful
                if (pokemon != null) {
                    pokemon = response.body()?.toPokemonInfo()
                    pokemonDao.updatePokemon(pokemon?.toPokemonInfoDbo()!!)
                } else {
                    pokemon = response.body()?.toPokemonInfo()
                    pokemonDao.addPokemon(pokemon?.toPokemonInfoDbo()!!)
                }
            }
        }

        return pokemon
    }

    private suspend fun getPokemons(pageIndex: Int, size: Int): List<Pokemon> =
        withContext(Dispatchers.IO) {
            val offset = pageIndex * DEFAULT_PAGE_SIZE
            if (isConnected.value) {
                val response = pokemonService.getPokemons(offset = offset, limit = size)
                if (response.isSuccessful)
                    return@withContext response.body()?.pokemons?.map { it.toPokemon() }
                        ?: emptyList()
                else throw HttpException(response)
            } else {
                return@withContext pokemonDao.getPokemons(offset = offset, limit = size)
                    .map { it.toPokemon() }
            }
        }

    private fun isOnline(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }
}