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
import com.example.data.cache.SharedPrefLoginAndPassword
import com.example.data.cache.SharedPreferencesLoginRememberMe
import com.example.movies.R
import com.example.movies.databinding.FragmentLoginBinding
import com.example.movies.utils.SIGNUP_WEB_PAGE_URL
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() =
            _binding ?: throw RuntimeException("FragmentLoginBinding == null")

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var loginSharedPreferencesRememberMe: SharedPreferencesLoginRememberMe

    @Inject
    lateinit var sharedPrefLoginAndPassword: SharedPrefLoginAndPassword

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        showSystemUI()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //checkIfLoginIsSuccess()
        //  skipLoginScreen()
        sendLoginData()
        rememberLoginAndPassword()
        openHomePage()
        openSignupWebPage()
        displayLoginErrorMessage()
    }

    /* private fun checkIfLoginIsSuccess() {
         viewModel.isLoginSuccess.observe(viewLifecycleOwner) {
             when (it) {
                 is ResponseResult.Failure -> {
                     binding.textError.visibility = View.VISIBLE
                     binding.textError.text = it.message
                 }
                 is ResponseResult.Success -> {
                     viewModel.saveSessionId()
                 }
             }
         }
     }*/

    private fun sendLoginData() {
        binding.btnLogin.setOnClickListener {
            viewModel.receiveLoginAndPassword(
                binding.editUserName.text.toString(),
                binding.editPassword.text.toString()
            )

        }
    }

    private fun openHomePage() {
        viewModel.isSessionIdSaved.observe(viewLifecycleOwner) { sessionIdSaved ->
            if (sessionIdSaved) {
                val isRememberMeChecked = loginSharedPreferencesRememberMe.loadIsRememberMeChecked()
                if (isRememberMeChecked) {
                    viewModel.saveUserNameAndPassword(
                        binding.editUserName.text.toString(),
                        binding.editPassword.text.toString()
                    )
                }
                findNavController().navigate(R.id.action_nav_login_fragment_to_homeFragment)
            }
        }
    }

    private fun rememberLoginAndPassword() {
        binding.checkboxRememberMe.setOnCheckedChangeListener { buttonView, isChecked ->
            loginSharedPreferencesRememberMe.saveIsRememberMeChecked(isChecked)
        }
    }

    private fun displayLoginErrorMessage() {
        viewModel.loginErrorText.observe(viewLifecycleOwner) { errorText ->
            if (errorText.isNotBlank()) {
                binding.textError.visibility = View.VISIBLE
                binding.textError.text = errorText
            }
        }
    }

    private fun openSignupWebPage() {
        binding.textSignup.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW).setData(Uri.parse(SIGNUP_WEB_PAGE_URL))
            startActivity(intent)
        }
    }

    private fun showSystemUI() {
        activity?.window?.decorView?.let {
            it.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }


}