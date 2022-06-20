package com.sacramento.movies.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sacramento.data.cache.SharedPrefLoginAndPassword
import com.sacramento.data.cache.SharedPreferencesLoginRememberMe
import com.sacramento.movies.R
import com.sacramento.movies.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() =
            _binding ?: throw RuntimeException("FragmentLoginBinding == null")

   // private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var loginSharedPreferencesRememberMe: SharedPreferencesLoginRememberMe

    @Inject
    lateinit var sharedPrefLoginAndPassword: SharedPrefLoginAndPassword

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //showSystemUI()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

       /* binding.editPassword.addTextChangedListener(loginTextWatcher)
        binding.editUserName.addTextChangedListener(loginTextWatcher)

        sendLoginData()
        rememberLoginAndPassword()
        openHomePage()
        openSignupWebPage()
        displayLoginErrorMessage()*/
    }

    /*private fun sendLoginData() {
        binding.btnLogin.setOnClickListener {
            binding.btnLogin.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
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
                binding.progressBar.visibility = View.GONE
                binding.textError.visibility = View.VISIBLE
                binding.textError.text = errorText
            }
        }
    }

    private val loginTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val userNameInput = binding.editUserName.text.toString().trim()
            val passwordInput = binding.editPassword.text.toString().trim()

            binding.btnLogin.isEnabled = userNameInput.isNotBlank() && passwordInput.isNotBlank()
        }

        override fun afterTextChanged(p0: Editable?) {
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
            WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
            WindowInsetsControllerCompat(
                this.requireActivity().window,
                it
            ).show(WindowInsetsCompat.Type.systemBars())
        }
    }*/

}