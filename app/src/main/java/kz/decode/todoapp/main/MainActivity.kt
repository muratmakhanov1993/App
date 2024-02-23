package kz.decode.todoapp.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.decode.todoapp.R
import kz.decode.todoapp.data.api.Api


class MainActivity : AppCompatActivity() {

    private var bottomNavigation: BottomNavigationView? = null
    private var navHost: NavHostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_navigation)
        navHost = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment

        navHost?.navController?.let { navController ->
            bottomNavigation?.setupWithNavController(navController)
        }
        val coroutineContext = SupervisorJob()+ Dispatchers.Main
        val coroutineScope = CoroutineScope(coroutineContext)
        val retrofitService = Api.getInstance()
        coroutineScope.launch {
            val catResponse = withContext(Dispatchers.IO){
                retrofitService.getRandomFact()
            }
            if(catResponse.isSuccessful){
                Log.d("test", catResponse.body().toString())
                val catInfoResponse = withContext(Dispatchers.IO){
                    retrofitService.getFactInfoById(catResponse.body()?._id.orEmpty())
                }
                Log.d("test 1", catInfoResponse.body().toString())
            }
        }

        coroutineScope.launch {
            val animal = withContext(Dispatchers.IO){
                retrofitService.getFactAnimal()
            }
            if(animal.isSuccessful){
                Log.d("test 2", animal.body().toString())
            }
        }
    }
}


