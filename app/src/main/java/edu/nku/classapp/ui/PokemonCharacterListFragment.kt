package edu.nku.classapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.nku.classapp.R
import edu.nku.classapp.databinding.FragmentPokemonListBinding
import edu.nku.classapp.ui.adapter.PokemonCharacterAdapter
import edu.nku.classapp.viewmodel.PokemonCharacterViewModel
import edu.nku.classapp.viewmodel.PokemonCharacterViewModel.PokemonCharacterState.Failure
import edu.nku.classapp.viewmodel.PokemonCharacterViewModel.PokemonCharacterState.Loading
import edu.nku.classapp.viewmodel.PokemonCharacterViewModel.PokemonCharacterState.Success
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonCharacterListFragment : Fragment() {

    private var _binding: FragmentPokemonListBinding? = null
    private val binding
        get() = _binding!!
    private val PokemonCharacterViewModel: PokemonCharacterViewModel by activityViewModels()
    private val PokemonCharacterAdapter = PokemonCharacterAdapter { pokemon, position ->
        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fragment_container_view,
                PokemonDetailFragment.newInstance(pokemon.url)
            )
            addToBackStack(null)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = PokemonCharacterAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        PokemonCharacterViewModel.fillData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            PokemonCharacterViewModel.pokemons.collect { event ->
                when (event) {
                    Failure -> {
                        binding.errorMessage.isVisible = true
                        binding.progressBar.isVisible = false
                        binding.recyclerView.isVisible = false
                    }

                    Loading -> {
                        binding.progressBar.isVisible = true
                        binding.recyclerView.isVisible = false
                        binding.errorMessage.isVisible = false
                    }

                    is Success -> {
                        PokemonCharacterAdapter.refreshData(event.pokemons)
                        binding.recyclerView.isVisible = true
                        binding.progressBar.isVisible = false
                        binding.errorMessage.isVisible = false
                        event.pokemons.forEach { pokemon ->
                            PokemonCharacterViewModel.fetchPokemonDetailForCard(pokemon.url)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            PokemonCharacterViewModel.pokemonDetailForCard.collect { cardResult ->
                if (cardResult.url.isNotEmpty() && cardResult.detail != null) {
                    PokemonCharacterAdapter.addPokemonCardInfo(
                        cardResult.url,
                        cardResult.detail
                    )
                }
            }
        }
    }

}