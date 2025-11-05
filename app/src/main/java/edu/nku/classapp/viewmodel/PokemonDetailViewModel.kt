package edu.nku.classapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.nku.classapp.data.model.PokemonDetailApiResponse
import edu.nku.classapp.data.repository.PokemonRepository
import edu.nku.classapp.model.PokemonDetailResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _pokemonDetail = MutableStateFlow<PokemonDetailState>(PokemonDetailState.Initial)
    val pokemonDetail: StateFlow<PokemonDetailState> = _pokemonDetail.asStateFlow()

    fun fetchPokemonDetails(url: String) {
        _pokemonDetail.value = PokemonDetailState.Loading
        viewModelScope.launch {
            when (val response = pokemonRepository.getPokemonDetails(url)) {
                is PokemonDetailApiResponse.Success -> {
                    _pokemonDetail.value = PokemonDetailState.Success(response.pokemonDetail)
                }

                PokemonDetailApiResponse.Error -> {
                    _pokemonDetail.value = PokemonDetailState.Error
                }
            }
        }
    }

    sealed class PokemonDetailState {
        object Initial : PokemonDetailState()
        object Loading : PokemonDetailState()
        data class Success(val pokemonDetail: PokemonDetailResponse) : PokemonDetailState()
        object Error : PokemonDetailState()
    }
}