package com.example.movies.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.utils.ResponseResult
import com.example.movies.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btn_login.setOnClickListener {
            viewModel.receiveLoginAndPassword(
                edit_user_name.text.toString(),
                edit_password.text.toString()
            )
        }

        rememberLoginAndPassword()
        checkIfLoginIsSuccess()
        openHomePage()
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
            if (sessionIdSaved){
                findNavController().navigate(R.id.action_nav_login_fragment_to_homeFragment)
            }
        }
    }

    private fun rememberLoginAndPassword() {
        checkbox_remember_me.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) println("mLog: true")
            else println("mLog: false")
        }
    }

}