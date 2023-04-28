package com.example.pokemonwiki.view

import android.app.Activity
import android.app.AppComponentFactory
import android.app.Application
import android.content.Context
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.pokemonwiki.R
import com.example.pokemonwiki.databinding.HomeLayoutBinding
import com.example.pokemonwiki.di.AppComponent
import com.example.pokemonwiki.di.DaggerAppComponent
import com.example.pokemonwiki.model.PokemonInfo
import com.example.pokemonwiki.view.fragment.AboutPokemonFragment
import com.example.pokemonwiki.view.fragment.PokemonsFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), Navigator {

    lateinit var binding: HomeLayoutBinding
    lateinit var mainFragment : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            mainFragment = PokemonsFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.navHostFragment, mainFragment)
                .commit()
        }
        NavigatorInstance.navigator = this
        }

    override fun showPokemonInfo(id : Long) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .hide(mainFragment)
            .add(R.id.navHostFragment, AboutPokemonFragment.newInstance(id))
            .commit()
    }

    override fun back() {
        onBackPressed()
    }


}