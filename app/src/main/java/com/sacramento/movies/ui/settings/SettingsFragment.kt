package com.sacramento.movies.ui.settings

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.sacramento.data.cache.SharedPreferencesLoginRememberMe
import com.sacramento.movies.R
import com.sacramento.movies.utils.LOG_OUT_KEY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(),Preference.OnPreferenceClickListener {

    @Inject
    lateinit var sharedPreferencesLoginRememberMe: SharedPreferencesLoginRememberMe

    private lateinit var buttonYes: Button
    private lateinit var buttonNo: Button

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.settings_background_color
            )
        )
        return view

    }

    override fun onStart() {
        logOut()
        super.onStart()
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            LOG_OUT_KEY -> showDialog()
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun showDialog() {
        Dialog(requireContext()).apply {
            setCancelable(false)
            setContentView(R.layout.logout_alert_dialog)
            window?.setBackgroundDrawable(ColorDrawable(0))

            buttonNo = findViewById(R.id.btn_no)
            buttonYes = findViewById(R.id.btn_yes)

            buttonYes.setOnClickListener {
                viewModel.logout()
                dismiss()
            }

            buttonNo.setOnClickListener { dismiss() }
        }.show()
    }

    private fun logOut() {
        viewModel.isLoggedOut.observe(viewLifecycleOwner) {
            if (it) findNavController().navigate(R.id.action_settings_to_authorizationFragment)
        }
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        return true
    }

}