package com.example.pokemonwiki.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.pokemonwiki.model.storage.model.PokemonDbo
import com.example.pokemonwiki.model.storage.model.PokemonInfoDbo
import java.io.ByteArrayOutputStream

internal fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

internal fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}

internal fun PokemonInfoDbo.toPokemonInfo() : PokemonInfo = PokemonInfo(
    id = id,
    image = image?.toBitmap(),
    name = name,
    height = height,
    weight = weight,
    types = types
)

internal fun PokemonDbo.toPokemon() : Pokemon = Pokemon(
    id = id,
    name = name
)

internal fun PokemonInfo.toPokemonInfoDbo() : PokemonInfoDbo = PokemonInfoDbo(
    id = id,
    image = image?.toByteArray(),
    name = name,
    height = height,
    weight = weight,
    types = types ?: emptyList()
)
