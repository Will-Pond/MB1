package edu.nku.classapp.data

import edu.nku.classapp.model.PokemonCharacterResponse
import edu.nku.classapp.model.PokemonDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url


interface PokemonApi {
    @GET("pokemon")
    suspend fun getPokemons(): Response<PokemonCharacterResponse>

    @GET
    suspend fun getPokemonDetails(@Url pokemonUrl: String): Response<PokemonDetailResponse>
}