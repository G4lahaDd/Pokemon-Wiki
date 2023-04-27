package com.example.pokemonwiki.model.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.pokemonwiki.model.Pokemon
import com.example.pokemonwiki.model.PokemonInfo
import com.example.pokemonwiki.model.remote.model.PokemonDto
import com.example.pokemonwiki.model.remote.model.PokemonInfoDto
import com.example.pokemonwiki.model.storage.model.PokemonDbo
import com.example.pokemonwiki.model.storage.model.PokemonInfoDbo
import java.io.ByteArrayOutputStream
import java.net.URL

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

internal fun PokemonDto.toPokemon() : Pokemon {
    val words = urlId?.split('/');
    val idStr = if(words?.last()!!.isEmpty()) words[words.size.minus(2)] else words.last()
    var id = idStr.toLong();
    return Pokemon( id = id, name = name)
}

internal fun PokemonInfo.toPokemonInfoDbo() : PokemonInfoDbo = PokemonInfoDbo(
    id = id,
    image = image?.toByteArray(),
    name = name,
    height = height,
    weight = weight,
    types = types ?: emptyList()
)

internal fun PokemonInfoDto.toPokemonInfo() : PokemonInfo = PokemonInfo(
    id = id?:0,
    image = getBitmapByUrl(imageUrl?.url),
    name = name,
    height = height,
    weight = weight,
    types = types.map{ it.typeName?.name ?: "" }
)


private fun getBitmapByUrl(url : String?) : Bitmap?{
    return try
    {
       BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
    }
    catch (ex: Exception) {
        null
    }
}