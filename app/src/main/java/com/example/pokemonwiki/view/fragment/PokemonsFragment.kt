package com.example.pokemonwiki.view.fragment

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemonwiki.R
import com.example.pokemonwiki.adapter.HolderAction
import com.example.pokemonwiki.adapter.LoadInfoStateAdapter
import com.example.pokemonwiki.databinding.FragmentPokemonsLayoutBinding
import com.example.pokemonwiki.di.AppComponent
import com.example.pokemonwiki.di.DaggerAppComponent
import com.example.pokemonwiki.model.PokemonInfo
import com.example.pokemonwiki.view.Navigator
import com.example.pokemonwiki.view.NavigatorInstance
import com.example.pokemonwiki.view.PokemonListener
import com.example.pokemonwiki.view.PokemonsAdapter
import com.example.pokemonwiki.view.appComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class PokemonsFragment : Fragment() {

    companion object {
        private const val ARG_POSITION = "ARG_POSITION"
        private const val ARG_VM = "ARG_VM"
        fun newInstance() = PokemonsFragment()
    }

    private lateinit var mainLoadStateHolder: LoadInfoStateAdapter.Holder
    private lateinit var binding : FragmentPokemonsLayoutBinding
    private lateinit var adapter:  PokemonsAdapter
    private lateinit var viewModel: PokemonsViewModel
    private lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPokemonsLayoutBinding.inflate(layoutInflater, container,false)
        if(savedInstanceState != null){
            val lastVisiblePosition = savedInstanceState.getInt(ARG_POSITION)
            binding.pokemonsRecyclerView.scrollToPosition(lastVisiblePosition)
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val lastVisiblePosition = (binding.pokemonsRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        outState.putInt(ARG_POSITION, lastVisiblePosition)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = context?.appComponent?.pokemonsViewModel!!
        setupPokemonsList()
        setupOfflineMode()
        navigator = NavigatorInstance.navigator
    }

    private fun setupPokemonsList() {
        adapter = PokemonsAdapter(this.requireContext(), object : PokemonListener {
            override fun onSelectPokemon(id : Long) {
                navigator.showPokemonInfo(id)
            }
        })

        // in case of loading errors this callback is called when you tap the 'Try Again' button
        val tryAgainAction: HolderAction = { adapter.retry() }
        // in case of loading errors this callback is called when you tap the 'Offline' button
        val switchOfflineModeAction: HolderAction = {
            viewModel.setOfflineMode()
        }

        val footerAdapter = LoadInfoStateAdapter(tryAgainAction,switchOfflineModeAction)

        // combined adapter which shows both the list of pokemons + footer indicator when loading pages
        val adapterWithLoadState = adapter.withLoadStateFooter(footerAdapter)

        binding.pokemonsRecyclerView.adapter = adapterWithLoadState
        (binding.pokemonsRecyclerView.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false

        mainLoadStateHolder = LoadInfoStateAdapter.Holder(
            binding.loadStateView,
            tryAgainAction,
            switchOfflineModeAction
        )

        observePokemons(adapter)
        observeLoadState(adapter)
    }

    /**
     *
     */
    private fun setupOfflineMode(){
        binding.offlineModeView.switchOfflineButton.setOnClickListener {
            viewModel.trySetOnlineMode()
            adapter.refresh()
        }
        lifecycleScope.launch {
            viewModel.isNetworkEnabled.collectLatest { value ->
                binding.offlineModeView.root.visibility = if(value) View.GONE else View.VISIBLE
            }
        }
    }

    private fun observePokemons(adapter: PokemonsAdapter) {
        lifecycleScope.launch{
            viewModel.pokemonsFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private fun observeLoadState(adapter: PokemonsAdapter) {
        lifecycleScope.launch {
            adapter.loadStateFlow.debounce(200).collectLatest { state ->
                // main indicator in the center of the screen
                mainLoadStateHolder.bind(state.refresh)
            }
        }
    }
}