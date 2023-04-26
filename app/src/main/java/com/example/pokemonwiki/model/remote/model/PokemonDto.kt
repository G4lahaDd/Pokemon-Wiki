package com.example.pokemonwiki.model.remote.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto(
    @SerializedName("name") val name: String?,
    @SerializedName("url") val urlId : String?
)