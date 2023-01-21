package com.sacramento.movies.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.sacramento.movies.R
import com.sacramento.movies.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private var _binding: ActivityMainBinding? = null
        private val binding: ActivityMainBinding
            get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")

        fun hideBottomNavBar() {
            binding.bottomNav.visibility = View.GONE
        }

        fun showBottomNavBar() {
            binding.bottomNav.visibility = View.VISIBLE
        }
    }


    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  if(savedInstanceState == null) checkIfAppLaunchedByNotification()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        hideBottomNavBar()
    }

    fun setupActionBar(toolbar: Toolbar, isTitleVisible: Boolean = true) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(isTitleVisible)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.moviesFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun hideBottomNavBar() {
        navController.addOnDestinationChangedListener { _, distination, _ ->
            when (distination.id) {
                R.id.welcome_screen, R.id.moviesFilterFragment, R.id.authorizationFragment, R.id.sessionSelectionFragment -> {
                    binding.bottomNav.visibility = View.GONE
                    binding.viewLine.visibility = View.GONE
                }
                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.viewLine.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (navController.currentDestination?.id == R.id.welcome_screen) finish()
    }

}