package edu.nku.classapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.nku.classapp.data.model.PokemonApiResponse
import edu.nku.classapp.data.model.PokemonDetailApiResponse
import edu.nku.classapp.data.repository.PokemonRepository
import edu.nku.classapp.model.PokemonCharacterResponse
import edu.nku.classapp.model.PokemonDetailResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonCharacterViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _pokemons = MutableStateFlow<PokemonCharacterState>(PokemonCharacterState.Loading)
    val pokemons: StateFlow<PokemonCharacterState> = _pokemons.asStateFlow()

    private val _pokemonDetailForCard = MutableStateFlow(
        PokemonDetailResponse.PokemonCardResult("", null)
    )
    val pokemonDetailForCard: StateFlow<PokemonDetailResponse.PokemonCardResult> =
        _pokemonDetailForCard.asStateFlow()

    fun fillData() = viewModelScope.launch {
        when (val response = pokemonRepository.getPokemons()) {
            is PokemonApiResponse.Success -> {
                _pokemons.value = PokemonCharacterState.Success(response.pokemons)
                response.pokemons.forEach { pokemon ->
                    fetchPokemonDetailForCard(pokemon.url)
                }
            }

            PokemonApiResponse.Error -> {
                _pokemons.value = PokemonCharacterState.Failure
            }
        }
    }

    fun fetchPokemonDetailForCard(url: String) = viewModelScope.launch {
        when (val response = pokemonRepository.getPokemonDetails(url)) {
            is PokemonDetailApiResponse.Success -> {
                _pokemonDetailForCard.value =
                    PokemonDetailResponse.PokemonCardResult(url, response.pokemonDetail)
            }

            PokemonDetailApiResponse.Error -> {

            }
        }
    }


    sealed class PokemonCharacterState {
        data class Success(val pokemons: List<PokemonCharacterResponse.Pokemon>) :
            PokemonCharacterState()

        data object Failure : PokemonCharacterState()
        data object Loading : PokemonCharacterState()
    }
}