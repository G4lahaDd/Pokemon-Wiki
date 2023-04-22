package com.example.pokemonwiki.model

import android.graphics.Bitmap

class PokemonInfo(
    val id: Long,
    val image : Bitmap?,
    val name: String?,
    val height: Int?,
    val weight: Int?,
    val types: List<String>?
)