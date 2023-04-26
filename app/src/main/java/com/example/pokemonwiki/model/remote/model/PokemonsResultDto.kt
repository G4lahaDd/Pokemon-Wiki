package com.example.pokemonwiki.model.remote.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonsResultDto(
    @SerializedName("results") val pokemons : List<PokemonDto>?
)