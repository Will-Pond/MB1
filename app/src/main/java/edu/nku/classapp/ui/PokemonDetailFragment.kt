package edu.nku.classapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import edu.nku.classapp.R
import edu.nku.classapp.databinding.FragmentCharacterDetailBinding
import edu.nku.classapp.viewmodel.PokemonDetailViewModel
import edu.nku.classapp.viewmodel.PokemonDetailViewModel.PokemonDetailState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {

    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding
        get() = _binding!!

    private val pokemonDetailViewModel: PokemonDetailViewModel by activityViewModels()
    private var pokemonUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pokemonUrl = it.getString(ARG_POKEMON_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        pokemonUrl?.let { pokemonDetailViewModel.fetchPokemonDetails(it) }
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            pokemonDetailViewModel.pokemonDetail.collect { state ->
                when (state) {
                    is PokemonDetailState.Success -> {
                        val details = state.pokemonDetail
                        binding.pokemonNameDetail.text =
                            binding.root.context.getString(R.string.name, details.name ?: "N/A")

                        details.sprites?.frontDefault?.let { imageUrl ->
                            Glide.with(requireContext())
                                .load(imageUrl)
                                .into(binding.pokemonImageDetail)
                        }


                        val types = details.types?.joinToString { it.type?.name ?: "" }
                        binding.pokemonTypeDetail.text =
                            binding.root.context.getString(R.string.type, types ?: "N/A")


                        val hpStat = details.stats?.find { it.stat?.name == "hp" }
                        binding.pokemonHpDetail.text =
                            binding.root.context.getString(
                                R.string.hp,
                                hpStat?.baseStat?.toString() ?: "N/A"
                            )

                        val attackStat = details.stats?.find { it.stat?.name == "attack" }
                        binding.pokemonAtkDetail.text =
                            binding.root.context.getString(
                                R.string.atk,
                                attackStat?.baseStat?.toString() ?: "N/A"
                            )


                        binding.pokemonWeightDetail.text =
                            binding.root.context.getString(
                                R.string.weight,
                                details.weight?.toString() ?: "N/A"
                            )

                    }

                    is PokemonDetailState.Loading -> {

                    }

                    is PokemonDetailState.Error -> {

                    }

                    PokemonDetailState.Initial -> {

                    }
                }
            }
        }
    }

    companion object {
        private const val ARG_POKEMON_URL = "pokemon_url"

        fun newInstance(pokemonUrl: String) = PokemonDetailFragment().apply {
            arguments = bundleOf(ARG_POKEMON_URL to pokemonUrl)
        }
    }
}