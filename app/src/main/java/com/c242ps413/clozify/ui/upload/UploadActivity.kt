package com.c242ps413.clozify.ui.upload

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.c242ps413.clozify.data.api.response.MoodResponse
import com.c242ps413.clozify.data.api.retrofit.ApiConfig
import com.c242ps413.clozify.data.retrofit.ApiService
import com.c242ps413.clozify.databinding.ActivityUploadBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString)
            binding.previewImageView.setImageURI(imageUri)
        } else {
            Toast.makeText(this, "No image to display", Toast.LENGTH_SHORT).show()
        }

        binding.moodbutton.setOnClickListener {
            imageUri?.let { uri ->
                uploadImageToServer(uri)
            } ?: run {
                Toast.makeText(this, "Image not found!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToServer(uri: Uri) {
        val file = uriToFile(uri)
        if (file != null) {
            val fileName = getFileNameFromUri(uri)

            binding.progressIndicator.visibility = android.view.View.VISIBLE

            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val apiService: ApiService = ApiConfig.getApiServiceMood()
                    val response: MoodResponse = apiService.uploadImage(body)

                    withContext(Dispatchers.Main) {
                        binding.progressIndicator.visibility = android.view.View.GONE

                        binding.textViewDeskripsi.text = response.predicted_mood ?: "Unknown mood"
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        binding.progressIndicator.visibility = android.view.View.GONE
                        Toast.makeText(this@UploadActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e("UploadActivity", "Error uploading image", e)
                    }
                }
            }
        } else {
            Toast.makeText(this, "Failed to process image file!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uriToFile(uri: Uri): File? {
        val contentResolver = contentResolver
        val tempFile = File.createTempFile("temp_", ".jpg", cacheDir)

        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            tempFile
        } catch (e: Exception) {
            Log.e("UploadActivity", "Failed to convert URI to File", e)
            null
        }
    }

    private fun getFileNameFromUri(uri: Uri): String {
        return uri.lastPathSegment ?: "Unknown Filename"
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}
