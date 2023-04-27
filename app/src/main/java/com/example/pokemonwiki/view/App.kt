package com.example.pokemonwiki.view

import android.app.Application
import android.content.Context
import com.example.pokemonwiki.di.AppComponent
import com.example.pokemonwiki.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent : AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this);
    }
}

val Context.appComponent : AppComponent
    get(){
        return when(this){
            is App -> appComponent
            else -> this.applicationContext.appComponent
        }
    }
