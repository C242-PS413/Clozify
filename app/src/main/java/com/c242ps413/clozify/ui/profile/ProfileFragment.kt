package com.c242ps413.clozify.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.c242ps413.clozify.R
import com.c242ps413.clozify.databinding.FragmentProfileBinding
import com.c242ps413.clozify.ui.profile.editprofile.ProfileUpdateActivity
import com.c242ps413.clozify.ui.profile.settingdarkmode.SettingPreferences
import com.c242ps413.clozify.ui.profile.settingdarkmode.SettingsViewModel
import com.c242ps413.clozify.ui.profile.settingdarkmode.SettingsViewModelFactory
import com.c242ps413.clozify.ui.profile.settingdarkmode.dataStore
import com.google.android.material.switchmaterial.SwitchMaterial
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var switchTheme: SwitchMaterial
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val profileFactory = ProfileViewModelFactory(requireActivity().application)
        profileViewModel = ViewModelProvider(this, profileFactory).get(ProfileViewModel::class.java) // Correct ViewModel

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val settingsPreferences = SettingPreferences.getInstance(requireContext().dataStore)
        val factory = SettingsViewModelFactory(settingsPreferences)
        settingsViewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)

        switchTheme = binding.switchTheme
        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.saveThemeSetting(isChecked)
        }

        binding.EditProfile.setOnClickListener {
            val intent = Intent(requireContext(), ProfileUpdateActivity::class.java)
            startActivity(intent)
        }

        observeProfileData()

        return root
    }

    private fun observeProfileData() {
        profileViewModel.getAllProfile().observe(viewLifecycleOwner) { profiles ->
            if (profiles.isNotEmpty()) {
                val profile = profiles[0]
                binding.profilename.text = profile.username
                binding.profilegender.text = profile.gender
                binding.profilelocation.text = profile.location

                profile.imgProfile?.let { imagePath ->
                    val file = File(imagePath)
                    if (file.exists()) {
                        Glide.with(requireContext())
                            .load(file)
                            .into(binding.profileimage)
                    } else {
                        Glide.with(requireContext())
                            .load(R.drawable.clozify_rb)
                            .into(binding.profileimage)
                    }
                } ?: run {
                    Glide.with(requireContext())
                        .load(R.drawable.clozify_rb) // A default image
                        .into(binding.profileimage)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        observeProfileData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
