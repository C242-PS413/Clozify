package com.c242ps413.clozify.ui.profile.editprofile

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Geocoder
import android.location.Location
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.c242ps413.clozify.R
import com.c242ps413.clozify.data.databases.profile.Profile
import com.c242ps413.clozify.databinding.ActivityProfileUpdateBinding
import com.c242ps413.clozify.ui.profile.ProfileFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Locale

class ProfileUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileUpdateBinding
    private var currentImageUri: Uri? = null
    private var existingImagePath: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: ProfileUpdateViewModel

    // Inisialisasi variabel untuk memantau perubahan pada field-field
    private var isUsernameFilled = false
    private var isLocationFilled = false
    private var isGenderSelected = false
    private var isProfileImageFilled = false // Tambahkan status gambar profil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ProfileUpdateViewModelFactory(application)).get(ProfileUpdateViewModel::class.java)

        val profileId = 1
        viewModel.getProfileById(profileId).observe(this, Observer { profile ->
            binding.progressBar.visibility = ProgressBar.GONE
            if (profile != null) {
                binding.editTextName.setText(profile.username)
                binding.editTextLocation.setText(profile.location)

                existingImagePath = profile.imgProfile

                profile.imgProfile?.let { imagePath ->
                    val bitmap = BitmapFactory.decodeFile(imagePath)
                    binding.profileimageupdate.setImageBitmap(bitmap)
                }

                when (profile.gender) {
                    "Women" -> binding.radioGroupGender.check(R.id.radioFemale)
                    "Men" -> binding.radioGroupGender.check(R.id.radioMale)
                    else -> binding.radioGroupGender.clearCheck()
                }

                // Memperbarui status untuk gambar dan data lainnya
                isUsernameFilled = profile.username.isNotBlank()
                isLocationFilled = profile.location.isNotBlank()
                isGenderSelected = profile.gender.isNotBlank()
                isProfileImageFilled = profile.imgProfile != null
            } else {
                // Jika tidak ada profil yang ditemukan, setel semuanya ke status "belum terisi"
                isUsernameFilled = false
                isLocationFilled = false
                isGenderSelected = false
                isProfileImageFilled = false
            }

            // Update status tombol simpan
            updateSaveButtonState()
        })

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.GetLocation.setOnClickListener {
            getLocation()
        }

        binding.SaveProfile.setOnClickListener {
            val username = binding.editTextName.text.toString()
            val location = binding.editTextLocation.text.toString()

            val selectedGenderId = binding.radioGroupGender.checkedRadioButtonId
            val gender = when (selectedGenderId) {
                R.id.radioFemale -> "Women"
                R.id.radioMale -> "Men"
                else -> null
            }

            val imageFilePath = currentImageUri?.let { uri -> uriToJpegFilePath(uri) } ?: existingImagePath ?: ""

            val profile = Profile(
                id = profileId,
                imgProfile = imageFilePath,
                username = username,
                location = location,
                gender = gender ?: ""
            )

            saveOrUpdateProfile(profile)
        }

        binding.fabCamera.setOnClickListener {
            startGallery()
        }

        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isUsernameFilled = s?.isNotBlank() ?: false
                updateSaveButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.editTextLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isLocationFilled = s?.isNotBlank() ?: false
                updateSaveButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.radioGroupGender.setOnCheckedChangeListener { _, checkedId ->
            isGenderSelected = checkedId != -1
            updateSaveButtonState()
        }

        binding.profileimageupdate.setOnClickListener {
            isProfileImageFilled = true
            updateSaveButtonState()
        }
    }

    private fun updateSaveButtonState() {
        val isAllFieldsFilled = isUsernameFilled && isLocationFilled && isGenderSelected && isProfileImageFilled
        binding.SaveProfile.isEnabled = isAllFieldsFilled
    }

    private fun saveOrUpdateProfile(profile: Profile) {
        val profileId = profile.id
        viewModel.getProfileById(profileId).observe(this, Observer { existingProfile ->
            if (existingProfile != null) {
                // Update profile
                viewModel.update(profile)
                Toast.makeText(this, "Profile successfully updated", Toast.LENGTH_SHORT).show()

                val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                val profileFragment = ProfileFragment()
                fragmentTransaction.replace(R.id.fragment_container, profileFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            } else {
                viewModel.insert(profile)
                Toast.makeText(this, "New profile added", Toast.LENGTH_SHORT).show()
            }
            binding.progressBar.visibility = ProgressBar.GONE
        })
    }

    private fun getLocation() {
        binding.progressBar.visibility = ProgressBar.VISIBLE
        binding.editTextLocation.text?.clear()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                getCityFromLocation(location.latitude, location.longitude)
            } else {
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = ProgressBar.GONE
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun getCityFromLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val cityName = addresses[0].subAdminArea?.replace("Kota ", "") ?: "City name not found"
                binding.editTextLocation.setText(cityName)
            } else {
                binding.editTextLocation.hint = "Insert your city"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            binding.editTextLocation.hint = "Geocoder Error"
        } finally {
            binding.progressBar.visibility = ProgressBar.GONE
        }
    }

    // Gallery image picker
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            // Konversi gambar yang dipilih menjadi byte array
            val byteArray = uriToByteArrayWithOrientation(uri)
            showImage(byteArray)

            // Memperbarui status gambar profil
            isProfileImageFilled = true
            updateSaveButtonState() // Pastikan tombol simpan diperbarui
        }
    }

    private fun showImage(byteArray: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        binding.profileimageupdate.setImageBitmap(bitmap)
    }

    private fun uriToByteArrayWithOrientation(uri: Uri): ByteArray {
        val contentResolver: ContentResolver = contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val byteArrayOutputStream = ByteArrayOutputStream()

        inputStream?.use { stream ->
            val buffer = ByteArray(1024)
            var length: Int
            while (stream.read(buffer).also { length = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, length)
            }
        }

        val orientation = getExifOrientation(uri)
        val byteArray = byteArrayOutputStream.toByteArray()

        return if (orientation != 0) {
            adjustImageOrientation(byteArray, orientation)
        } else {
            byteArray
        }
    }

    private fun getExifOrientation(uri: Uri): Int {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val exif = ExifInterface(inputStream)
            return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        }
        return ExifInterface.ORIENTATION_NORMAL
    }

    private fun adjustImageOrientation(byteArray: ByteArray, orientation: Int): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val matrix = Matrix()

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        val outputStream = ByteArrayOutputStream()
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }

    private fun uriToJpegFilePath(uri: Uri): String? {
        val contentResolver: ContentResolver = contentResolver
        val fileName = "profile_image_${System.currentTimeMillis()}.jpg"
        val file = File(cacheDir, fileName)

        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } != -1) {
                        outputStream.write(buffer, 0, length)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath
    }
}