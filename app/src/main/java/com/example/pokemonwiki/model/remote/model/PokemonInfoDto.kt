package com.example.pokemonwiki.model.remote.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonInfoDto(
    @SerializedName("id") val id : Long?,
    @SerializedName("sprites") val imageUrl :UrlDto?,
    @SerializedName("name") val name : String?,
    @SerializedName("height") val height : Int?,
    @SerializedName("weight") val weight : Int?,
    @SerializedName("types") val types : List<TypeDto>

)

@Serializable
data class UrlDto(
    @SerializedName("front_default") val url : String?
)

@Serializable
data class TypeDto(
    @SerializedName("type") val typeName : TypeNameDto?
)

@Serializable
data class TypeNameDto(
    @SerializedName("name") val name : String?
)