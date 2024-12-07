package com.c242ps413.clozify.ui.upload

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.c242ps413.clozify.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive image URI
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            binding.previewImageView.setImageURI(imageUri)

            // Extract the filename from the URI
            val fileName = getFileNameFromUri(imageUri)

            // Display the filename in the TextView (mendapatkan nama file photo)
            //binding.textViewDeskripsi.text = "$fileName"
        } else {
            Toast.makeText(this, "No image to display", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileNameFromUri(uri: Uri): String {
        return uri.lastPathSegment ?: "Unknown Filename"
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}
