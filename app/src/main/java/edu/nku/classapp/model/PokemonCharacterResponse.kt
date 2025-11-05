package edu.nku.classapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonCharacterResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    @Json(name = "results")
    val pokemons: List<Pokemon>
) {
    @JsonClass(generateAdapter = true)
    data class Pokemon(
        val name: String,
        val url: String,
    )
}

@JsonClass(generateAdapter = true)
data class PokemonDetailResponse(
    val sprites: Sprites?,
    val name: String?,
    val types: List<TypeSlot>?,
    val stats: List<Stat>?,
    val weight: Int?
) {
    @JsonClass(generateAdapter = true)
    data class Sprites(
        @Json(name = "front_default") val frontDefault: String?
    )

    @JsonClass(generateAdapter = true)
    data class TypeSlot(
        val slot: Int?,
        val type: PokemonType?
    )

    @JsonClass(generateAdapter = true)
    data class PokemonType(
        val name: String?,
        val url: String?
    )

    @JsonClass(generateAdapter = true)
    data class Stat(
        @Json(name = "base_stat") val baseStat: Int?,
        val stat: StatDetail?
    )

    @JsonClass(generateAdapter = true)
    data class StatDetail(
        val name: String?,
        val url: String?
    )

    @JsonClass(generateAdapter = true)
    data class PokemonCardResult(
        val url: String, val detail: PokemonDetailResponse?
    )
}







