package com.example.pokemonwiki.view.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.pokemonwiki.R
import com.example.pokemonwiki.databinding.FragmentAboutPokemonLayoutBinding
import com.example.pokemonwiki.model.PokemonInfo
import com.example.pokemonwiki.view.NavigatorInstance
import com.example.pokemonwiki.view.appComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AboutPokemonFragment : Fragment() {

    companion object {
        private const val ARG_POKEMON_ID = "ARG_POKEMON_ID"

        /**
         * Create fragment for Pokemon with [id] and add it to bundle
         */
        fun newInstance(id: Long): Fragment {
            val fragment = AboutPokemonFragment()
            fragment.arguments = bundleOf(ARG_POKEMON_ID to id)
            return fragment
        }
    }

    private lateinit var viewModel: AboutPokemonViewModel
    private lateinit var binding: FragmentAboutPokemonLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutPokemonLayoutBinding.inflate(layoutInflater, container, false)
        binding.backButton.setOnClickListener { NavigatorInstance.navigator.back() }

        viewModel  = context?.appComponent?.aboutPokemonViewModel!!

        viewModel.loadPokemonInfo(requireArguments().getLong(ARG_POKEMON_ID))

        viewModel.pokemonInfo.observe(viewLifecycleOwner, Observer {
            bind(it)
        })
        return binding.root
    }


    fun bind(pokemonInfo: PokemonInfo) = with(binding) {
        pokemonNameText.text = pokemonInfo.name
        pokemonHeightText.text = "${pokemonInfo.height} ${getString(R.string.height_units)}"
        pokemonWeightText.text = "${pokemonInfo.weight} ${getString(R.string.weight_units)}"
        pokemonTypeText.text = pokemonInfo.types?.joinToString(", ") { it }
        imageView.setImageBitmap(pokemonInfo.image)
    }


}