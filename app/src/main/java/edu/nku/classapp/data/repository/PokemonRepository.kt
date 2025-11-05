package edu.nku.classapp.data.repository

import edu.nku.classapp.data.model.PokemonApiResponse
import edu.nku.classapp.data.model.PokemonDetailApiResponse

interface PokemonRepository {
    suspend fun getPokemons(): PokemonApiResponse
    suspend fun getPokemonDetails(url: String): PokemonDetailApiResponse

}