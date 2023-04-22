package com.example.pokemonwiki.model.storage.model

import android.graphics.Bitmap
import android.graphics.Picture
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokemonwiki.model.PokemonInfo

@Entity(tableName = "pokemons")
data class PokemonInfoDbo(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB) val image : ByteArray?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "height") val height: Int?,
    @ColumnInfo(name = "weight") val weight: Int?,
    @ColumnInfo(name = "types") val types: List<String>)