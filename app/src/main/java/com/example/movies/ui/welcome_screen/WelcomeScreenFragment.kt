package com.example.movies.ui.welcome_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.data.cache.SharedPrefMovieCategory
import com.example.data.cache.SharedPrefMovieFilter
import com.example.data.cache.SharedPreferencesLoginRememberMe
import com.example.data.cache.clearMovieFilterCache
import com.example.movies.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeScreenFragment : Fragment() {

    @Inject
    lateinit var isLoginRememberMe: SharedPreferencesLoginRememberMe

    @Inject
    lateinit var sharedPrefMovieFilter: SharedPrefMovieFilter

    @Inject lateinit var sharedPrefMovieCategory: SharedPrefMovieCategory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPrefMovieCategory.clearMovieCategory()
        clearMovieFilterCache(sharedPrefMovieFilter)
        delayWelcomeScreen()
    }

    private fun delayWelcomeScreen() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000)
            if (isLoginRememberMe.loadIsRememberMeChecked()) {
                findNavController().navigate(R.id.action_welcomeScreenFragment_to_homeFragment)
            } else {
                findNavController().navigate(R.id.action_welcomeScreenFragment_to_nav_login_fragment)
            }

        }
    }

}