package edu.nku.classapp.data.model

import edu.nku.classapp.model.PokemonCharacterResponse
import edu.nku.classapp.model.PokemonDetailResponse

sealed class PokemonApiResponse {
    data class Success(val pokemons: List<PokemonCharacterResponse.Pokemon>) : PokemonApiResponse()
    data object Error : PokemonApiResponse()
}

sealed class PokemonDetailApiResponse {
    data class Success(val pokemonDetail: PokemonDetailResponse) : PokemonDetailApiResponse()
    data object Error : PokemonDetailApiResponse()
}