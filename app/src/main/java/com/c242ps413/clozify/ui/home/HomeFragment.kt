package com.c242ps413.clozify.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.c242ps413.clozify.data.api.retrofit.ApiConfig
import com.c242ps413.clozify.data.model.dummy.DummyData
import com.c242ps413.clozify.data.repository.ProfileRepository
import com.c242ps413.clozify.databinding.FragmentHomeBinding
import cz.msebera.android.httpclient.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireActivity().application
        val repository = ProfileRepository(application)
        val homeViewModel = ViewModelProvider(this, HomeViewModelFactory(repository))[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize Adapter for Recommendations
        homeAdapter = HomeAdapter()
        binding.recyclerView.apply {
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }

        // Observe Profile Data
        homeViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                binding.textHome.text = "Hi, ${profile.username}!"
                binding.locationlabel.text = profile.location

                // Load profile image
                Glide.with(this)
                    .load(profile.imgProfile)
                    .circleCrop()
                    .into(binding.profileImage)

                // Fetch Weather
                fetchWeather(profile.location)

                // Fetch Recommendations
                fetchRecommendations()
            }
        }

        return root
    }

    private fun fetchWeather(city: String) {
        lifecycleScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.getCurrentWeather(city, "cafea1a8d3c32cd638c250d344272776") // API key

                // Periksa apakah response berhasil
                if (response.weather != null && response.weather.isNotEmpty() && response.main?.temp != null) {
                    // Jika respons berhasil, update UI
                    val currentWeather = response.weather[0]?.main
                    val description = response.weather[0]?.description
                    val tempInKelvin = response.main?.temp

                    // Convert ke Celsius
                    val tempInCelsius = tempInKelvin?.minus(273.15) // Konversi ke Celsius
                    val temperature: String = DecimalFormat("##.##").format(tempInCelsius)

                    // Update UI dengan data cuaca
                    withContext(Dispatchers.Main) {
                        binding.temperatureLabel.text = "$currentWeather $temperature°C"
                    }
                } else {
                    // Jika data cuaca tidak ditemukan
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Data cuaca tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: HttpException) {
                // Tangani error HTTP
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "HTTP Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Tangani error lainnya seperti kesalahan jaringan
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchRecommendations() {
        try {
            val recommendations = DummyData.recommendations
            Log.d("HomeFragment", "Recommendations: $recommendations") // Log data rekomendasi
            homeAdapter.submitList(recommendations)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
