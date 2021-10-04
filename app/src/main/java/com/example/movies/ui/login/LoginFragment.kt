package com.example.movies.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.utils.ResponseResult
import com.example.movies.R
import com.example.movies.utils.SIGNUP_WEB_PAGE_URL
import com.example.data.cache.SharedPrefLoginAndPassword
import com.example.data.cache.SharedPreferencesLoginRememberMe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var loginSharedPreferencesRememberMe: SharedPreferencesLoginRememberMe

    @Inject
    lateinit var sharedPrefLoginAndPassword: SharedPrefLoginAndPassword

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        showSystemUI()
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btn_login.setOnClickListener {
            viewModel.receiveLoginAndPassword(
                edit_user_name.text.toString(),
                edit_password.text.toString()
            )
        }

        skipLoginScreen()
        rememberLoginAndPassword()
        checkIfLoginIsSuccess()
        openHomePage()
        openSignupWebPage()
    }

    private fun checkIfLoginIsSuccess() {
        viewModel.isLoginSuccess.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseResult.Failure -> {
                    text_error.visibility = View.VISIBLE
                    text_error.text = it.message
                }
                is ResponseResult.Success -> {
                    viewModel.saveSessionId()
                }
            }
        }
    }

    private fun openHomePage() {
        viewModel.isSessionIdSaved.observe(viewLifecycleOwner) { sessionIdSaved ->
            if (sessionIdSaved) {
                val isRememberMeChecked = loginSharedPreferencesRememberMe.loadIsRememberMeChecked()
                if (isRememberMeChecked) {
                    sharedPrefLoginAndPassword.saveUserName(edit_user_name.text.toString())
                    sharedPrefLoginAndPassword.savePassword(edit_password.text.toString())
                }
                findNavController().navigate(R.id.action_nav_login_fragment_to_homeFragment)
            }
        }
    }

    private fun rememberLoginAndPassword() {
        checkbox_remember_me.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                loginSharedPreferencesRememberMe.saveIsRememberMeChecked(isChecked)
            } else {
                loginSharedPreferencesRememberMe.saveIsRememberMeChecked(false)
            }
        }
    }

    private fun skipLoginScreen() {
        viewModel.isRememberMeChecked.observe(viewLifecycleOwner) {
            if (it) findNavController().navigate(R.id.action_nav_login_fragment_to_homeFragment)
        }
    }

    private fun openSignupWebPage() {
        text_signup.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW).setData(Uri.parse(SIGNUP_WEB_PAGE_URL))
            startActivity(intent)
        }
    }

    private fun showSystemUI() {
        activity?.window?.decorView?.let {
            it.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }


}