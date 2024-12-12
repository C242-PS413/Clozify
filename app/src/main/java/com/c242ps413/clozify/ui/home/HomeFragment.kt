package com.c242ps413.clozify.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.c242ps413.clozify.R
import com.c242ps413.clozify.data.api.response.RecommendationItem
import com.c242ps413.clozify.data.api.retrofit.ApiConfig
import com.c242ps413.clozify.data.model.dummy.DummyData
import com.c242ps413.clozify.data.repository.FavoriteRepository
import com.c242ps413.clozify.data.repository.ProfileRepository
import com.c242ps413.clozify.databinding.FragmentHomeBinding
import com.c242ps413.clozify.ui.UserDataHolder
import com.c242ps413.clozify.ui.upload.CameraActivity
import com.c242ps413.clozify.ui.upload.CameraActivity.Companion.CAMERAX_RESULT
import com.c242ps413.clozify.ui.upload.UploadActivity
import cz.msebera.android.httpclient.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeAdapter: HomeAdapter
    private val allRecommendations = DummyData.recommendations

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(requireContext(), REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val application = requireActivity().application
        val profileRepository = ProfileRepository(application)
        val favoriteRepository = FavoriteRepository(application)
        val homeViewModel = ViewModelProvider(
            this, HomeViewModelFactory(profileRepository, favoriteRepository)
        )[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.getbutton.isEnabled = false

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.getbutton.setOnClickListener { startCameraX() }

        homeAdapter = HomeAdapter(object : HomeAdapter.OnItemClickListener {
            override fun onItemClick(event: RecommendationItem) {
                Toast.makeText(requireContext(), "${event.name}", Toast.LENGTH_SHORT).show()
            }
        }, requireActivity().application)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }

        binding.radioGroupOutfit.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioTopwear -> updateRecyclerView(allRecommendations.topWear.flatMap { it.moreRecommendedItems })
                R.id.radioBottomwear -> updateRecyclerView(allRecommendations.bottomwear.flatMap { it.recommendedItems })
                R.id.radioFootwear -> updateRecyclerView(allRecommendations.footwear.flatMap { it.moreRecommendedItems })
            }
        }

        binding.radioGroupOutfit.check(R.id.radioTopwear)
        updateRecyclerView(allRecommendations.topWear.flatMap { it.moreRecommendedItems })

        homeViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                binding.textHome.text = "Hi, ${profile.username}!"
                binding.locationlabel.text = profile.location

                UserDataHolder.userData.gender = profile.gender

                Glide.with(this)
                    .load(profile.imgProfile)
                    .circleCrop()
                    .into(binding.profileImage)

                fetchWeather(profile.location)

                fetchRecommendations()
            }
        }

        return root
    }

    private fun updateRecyclerView(items: List<RecommendationItem>) {
        homeAdapter.submitList(items)
    }

    private fun fetchWeather(city: String) {
        binding.getbutton.isEnabled = false
        lifecycleScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.getCurrentWeather(city, "cafea1a8d3c32cd638c250d344272776") // API key

                if (response.weather != null && response.weather.isNotEmpty() && response.main?.temp != null) {
                    val currentWeather = response.weather[0]?.main
                    val description = response.weather[0]?.description
                    val tempInKelvin = response.main?.temp

                    val tempInCelsius = tempInKelvin?.minus(273.15)
                    val temperature: Int = tempInCelsius?.roundToInt() ?: 0

                    val weatherCategory = if (temperature >= 28) {
                        "Sunny"
                    } else {
                        "Rainy"
                    }

                    withContext(Dispatchers.Main) {
                        binding.temperatureLabel.text = "$currentWeather $temperature°C"
                        UserDataHolder.userData.season = weatherCategory
                        binding.getbutton.isEnabled = true

                        fetchRecommendations()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Weather Not Found", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "HTTP Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchRecommendations() {
        lifecycleScope.launch {
            try {
                val recommendations = allRecommendations

                updateRecyclerView(recommendations.topWear.flatMap { it.moreRecommendedItems })

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to get recommendation: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CAMERAX_RESULT) {
            val imageUri = result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            if (imageUri != null) {
                val uploadIntent = Intent(requireContext(), UploadActivity::class.java)
                uploadIntent.putExtra(UploadActivity.EXTRA_IMAGE_URI, imageUri.toString())
                startActivity(uploadIntent)
            } else {
                Toast.makeText(requireContext(), "Failed to get image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}

/*class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeAdapter: HomeAdapter
    private val allRecommendations = DummyData.recommendations

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(requireContext(), REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val application = requireActivity().application
        val profileRepository = ProfileRepository(application)
        val favoriteRepository = FavoriteRepository(application)
        val homeViewModel = ViewModelProvider(
            this, HomeViewModelFactory(profileRepository, favoriteRepository)
        )[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.getbutton.setOnClickListener { startCameraX() }

        homeAdapter = HomeAdapter(object : HomeAdapter.OnItemClickListener {
            override fun onItemClick(event: RecommendationItem) {
                Toast.makeText(requireContext(), "${event.name}", Toast.LENGTH_SHORT).show()
            }
        }, requireActivity().application)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }

        binding.radioGroupOutfit.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioTopwear -> updateRecyclerView(allRecommendations.topWear.flatMap { it.moreRecommendedItems })
                R.id.radioBottomwear -> updateRecyclerView(allRecommendations.bottomwear.flatMap { it.recommendedItems })
                R.id.radioFootwear -> updateRecyclerView(allRecommendations.footwear.flatMap { it.moreRecommendedItems })
            }
        }

        binding.radioGroupOutfit.check(R.id.radioTopwear)
        updateRecyclerView(allRecommendations.topWear.flatMap { it.moreRecommendedItems })

        homeViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                binding.textHome.text = "Hi, ${profile.username}!"
                binding.locationlabel.text = profile.location

                UserDataHolder.userData.gender = profile.gender

                Glide.with(this)
                    .load(profile.imgProfile)
                    .circleCrop()
                    .into(binding.profileImage)

                fetchWeather(profile.location)

                fetchRecommendations()
            }
        }

        return root
    }

    private fun updateRecyclerView(items: List<RecommendationItem>) {
        homeAdapter.submitList(items)
    }

    private fun fetchWeather(city: String) {
        lifecycleScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.getCurrentWeather(city, "cafea1a8d3c32cd638c250d344272776") // API key

                if (response.weather != null && response.weather.isNotEmpty() && response.main?.temp != null) {
                    val currentWeather = response.weather[0]?.main
                    val description = response.weather[0]?.description
                    val tempInKelvin = response.main?.temp

                    val tempInCelsius = tempInKelvin?.minus(273.15)
                    val temperature: Int = tempInCelsius?.roundToInt() ?: 0

                    val weatherCategory = if (temperature >= 28) {
                        "Sunny"
                    } else {
                        "Rainy"
                    }

                    withContext(Dispatchers.Main) {
                        binding.temperatureLabel.text = "$currentWeather $temperature°C"
                        UserDataHolder.userData.season = weatherCategory
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Weather Not Found", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "HTTP Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchRecommendations() {
        lifecycleScope.launch {
            try {
                val recommendations = allRecommendations

                updateRecyclerView(recommendations.topWear.flatMap { it.moreRecommendedItems })

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to get recommendation: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CAMERAX_RESULT) {
            val imageUri = result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            if (imageUri != null) {
                val uploadIntent = Intent(requireContext(), UploadActivity::class.java)
                uploadIntent.putExtra(UploadActivity.EXTRA_IMAGE_URI, imageUri.toString())
                startActivity(uploadIntent)
            } else {
                Toast.makeText(requireContext(), "Failed to get image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}*/