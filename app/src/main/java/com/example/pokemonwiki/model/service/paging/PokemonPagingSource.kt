package com.example.pokemonwiki.model.service.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pokemonwiki.model.Pokemon
import com.example.pokemonwiki.model.service.PokemonRepository

typealias PokemonsSource = suspend (pageIndex : Int, PageSize : Int) -> List<Pokemon>

class PokemonPagingSource (
    private val pokemonsSource: PokemonsSource,
    private val pageSize: Int) : PagingSource<Int, Pokemon>() {

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        // get the most recently accessed index in the users list:
        val anchorPosition = state.anchorPosition ?: return null
        // convert item index to page index:
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        // calculate 'currentKey'
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val pageIndex = params.key ?: 0

        try {
            val pokemons = pokemonsSource(pageIndex, params.loadSize)

            return LoadResult.Page(
                data = pokemons,
                prevKey = if (pageIndex == 0) null else pageIndex - 1 ,
                nextKey = if (pokemons.size == params.loadSize) pageIndex + (params.loadSize / pageSize) else null
            )
        }catch (ex : Exception){
            return LoadResult.Error(throwable = ex);
        }
    }
}