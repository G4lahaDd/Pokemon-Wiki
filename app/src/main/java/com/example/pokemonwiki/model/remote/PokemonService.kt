package com.example.pokemonwiki.model.remote

import com.example.pokemonwiki.model.remote.model.PokemonDto
import com.example.pokemonwiki.model.remote.model.PokemonInfoDto
import com.example.pokemonwiki.model.remote.model.PokemonsResultDto
import com.example.pokemonwiki.model.service.PokemonRepository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    @GET("v2/pokemon")
    suspend fun getPokemons(
        @Query("offset")  offset: Int = 0,
        @Query("limit") limit: Int = PokemonRepository.DEFAULT_PAGE_SIZE,
    ): Response<PokemonsResultDto>

    @GET("v2/pokemon/{id}/")
    suspend fun getPokemonById(
        @Path("id") id: Int = 0,
    ): Response<PokemonInfoDto>

}