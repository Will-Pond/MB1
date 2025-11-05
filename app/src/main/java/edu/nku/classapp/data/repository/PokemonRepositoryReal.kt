package edu.nku.classapp.data.repository

import edu.nku.classapp.data.PokemonApi
import edu.nku.classapp.data.model.PokemonApiResponse
import edu.nku.classapp.data.model.PokemonDetailApiResponse
import javax.inject.Inject

class PokemonRepositoryReal @Inject constructor(
    private val pokemonApi: PokemonApi
) : PokemonRepository {
    override suspend fun getPokemons(): PokemonApiResponse {
        val result = pokemonApi.getPokemons()
        return if (result.isSuccessful) {
            result.body()?.let { PokemonApiResponse.Success(it.pokemons) }
                ?: PokemonApiResponse.Error
        } else {
            PokemonApiResponse.Error
        }
    }

    override suspend fun getPokemonDetails(url: String): PokemonDetailApiResponse {
        val result = pokemonApi.getPokemonDetails(url)
        return if (result.isSuccessful) {
            result.body()?.let { PokemonDetailApiResponse.Success(it) }
                ?: PokemonDetailApiResponse.Error
        } else {
            PokemonDetailApiResponse.Error
        }
    }
}