package com.sacramento.movies.ui.session

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sacramento.domain.utils.ResponseResult
import com.sacramento.movies.R
import com.sacramento.movies.databinding.FragmentSessionSelectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SessionSelectionFragment : Fragment() {

    private val viewModel: SessionSelectionViewModel by viewModels()

    private var _binding: FragmentSessionSelectionBinding? = null
    private val binding: FragmentSessionSelectionBinding
        get() = _binding ?: throw RuntimeException("FragmentSessionSelectionBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSessionSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToLoginPage()
        navigateToHomePageAsGuest()
    }

    private fun navigateToLoginPage() {
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.authorizationFragment)
        }
    }

    private fun navigateToHomePageAsGuest() {
        binding.btnGuest.setOnClickListener {
            viewModel.fetchGuestSession()
            openGuestSession()
        }
    }

    private fun openGuestSession() {
        viewModel.guestSession.observe(viewLifecycleOwner) {
            when (it) {
                ResponseResult.Loading -> binding.progressBarSession.visibility = View.VISIBLE
                is ResponseResult.Failure ->{
                    binding.progressBarSession.visibility = View.INVISIBLE
                    binding.texErrorSession.visibility = View.VISIBLE
                }
                is ResponseResult.Success ->{
                    binding.progressBarSession.visibility = View.INVISIBLE
                    binding.texErrorSession.visibility = View.INVISIBLE
                    viewModel.saveGuestSessionId(it.data.guestSessionId)
                    findNavController().navigate(R.id.homeFragment)
                }
            }
        }
    }

}
