package com.example.movies.ui.welcome_screen

import android.annotation.SuppressLint
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
            findNavController().popBackStack(R.id.welcome_screen,true)
            if (isLoginRememberMe.loadIsRememberMeChecked() && userName.isNotBlank() && password.isNotBlank()) {
                findNavController().navigate(R.id.homeFragment)
            } else {
                findNavController().navigate(R.id.nav_login_fragment)
            }

        }
    }

    private fun hideSystemUi(view: View) {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(requireActivity().window, view).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onStop() {
        super.onStop()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }

}