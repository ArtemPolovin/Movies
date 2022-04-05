package com.example.movies.ui.welcome_screen

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.data.cache.*
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

    @Inject
    lateinit var sharedPrefMovieCategory: SharedPrefMovieCategory

    @Inject
   lateinit var sharedPrefLoginAndPassword: SharedPrefLoginAndPassword

//    @Inject
//    lateinit var loginSharedPreferencesRememberMe: SharedPreferencesLoginRememberMe

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      // loginSharedPreferencesRememberMe.saveIsRememberMeChecked(false)
        hideSystemUi(view)
        sharedPrefMovieCategory.clearMovieCategory()
        sharedPrefMovieFilter.clearFilterCache()
        delayWelcomeScreen()
    }

    private fun delayWelcomeScreen() {
        viewLifecycleOwner.lifecycleScope.launch {
            val userName = sharedPrefLoginAndPassword.loadUserName()
            val password = sharedPrefLoginAndPassword.loadPassword()
            delay(3000)
            if (isLoginRememberMe.loadIsRememberMeChecked() && userName.isNotBlank() && password.isNotBlank()) {
                findNavController().navigate(R.id.action_welcome_screen_to_homeFragment)
            } else {
                findNavController().navigate(R.id.action_welcomeScreenFragment_to_nav_login_fragment)
            }

        }
    }

    private fun hideSystemUi(view: View) {
       // activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(requireActivity().window, view).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

    }

}