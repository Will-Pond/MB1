package edu.nku.classapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.nku.classapp.R
import edu.nku.classapp.databinding.PokemonCardViewBinding
import edu.nku.classapp.model.PokemonCharacterResponse
import edu.nku.classapp.model.PokemonDetailResponse

class PokemonCharacterAdapter(
    private val onCharacterClicked: (pokemon: PokemonCharacterResponse.Pokemon, position: Int) -> Unit,
) : RecyclerView.Adapter<PokemonCharacterAdapter.PokemonCharacterViewHolder>() {

    private val pokemonCharacters = mutableListOf<PokemonCharacterResponse.Pokemon>()
    private val pokemonCardInfo = mutableMapOf<String, PokemonDetailResponse>()

    class PokemonCharacterViewHolder(
        private val binding: PokemonCardViewBinding,
        private val onCharacterClicked: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onCharacterClicked(adapterPosition)
            }
        }

        fun bind(pokemon: PokemonCharacterResponse.Pokemon, pokemonDetail: PokemonDetailResponse?) {
            binding.pokemonName.text =
                binding.root.context.getString(R.string.name, pokemon.name)
            binding.pokemonUrl.text =
                binding.root.context.getString(R.string.url, pokemon.url)


            pokemonDetail?.sprites?.frontDefault?.let { imageUrl ->
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .into(binding.pokemonImage)
            }


            val hpStat = pokemonDetail?.stats?.find { it.stat?.name == "hp" }
            binding.pokemonHp.text =
                binding.root.context.getString(R.string.hp, hpStat?.baseStat ?: "N/A")


            val atkStat = pokemonDetail?.stats?.find { it.stat?.name == "attack" }
            binding.pokemonAtk.text =
                binding.root.context.getString(R.string.atk, atkStat?.baseStat ?: "N/A")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(pokemons: List<PokemonCharacterResponse.Pokemon>) {
        pokemonCharacters.clear()
        pokemonCharacters.addAll(pokemons)
        notifyDataSetChanged()

    }

    // adds more info to the card
    fun addPokemonCardInfo(url: String, detailResponse: PokemonDetailResponse) {
        pokemonCardInfo[url] = detailResponse
        val index = pokemonCharacters.indexOfFirst { it.url == url }
        if (index != -1) {
            notifyItemChanged(index)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonCharacterViewHolder {
        val binding =
            PokemonCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PokemonCharacterViewHolder(binding) { position ->
            onCharacterClicked(pokemonCharacters[position], position)
        }
    }

    override fun getItemCount() = pokemonCharacters.size

    override fun onBindViewHolder(
        holder: PokemonCharacterViewHolder,
        position: Int
    ) {
        val pokemon = pokemonCharacters[position]
        val pokemonCard = pokemonCardInfo[pokemon.url]
        holder.bind(pokemon, pokemonCard)
    }
}