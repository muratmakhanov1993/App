package kz.decode.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

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

    }
}