package com.dicoding.picodiploma.loginwithanimation.view.story.create

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityCreateBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.meone.storyapp.utils.getImageUri
import com.meone.storyapp.utils.reduceFileImage
import com.meone.storyapp.utils.uriToFile

class CreateStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private var currentImageUri: Uri? = null
    private var dialogLoading: Dialog? = null

    private val viewModel by viewModels<CreateStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }

        binding.uploadBtn.setOnClickListener {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                val description = binding.editTextDescription.text.toString()

                viewModel.createStory(imageFile, description)
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.response.observe(this) {
            if(!it.error) {
                AlertDialog.Builder(this).apply {
                    setTitle("Yeah!")
                    setMessage("Story Anda Berhasil Di upload!")
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            }
        }

        viewModel.guestResponse.observe(this) {
            if(!it.error) {
                AlertDialog.Builder(this).apply {
                    setTitle("Yeah!")
                    setMessage("Story Anda Berhasil Di upload Sebagai Guest!")
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            Log.d("Image URI", "showImage: $uri")
            binding.imagePriview.setImageURI(uri)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            if (dialogLoading == null) {
                dialogLoading = Dialog(this)
                dialogLoading!!.setContentView(R.layout.loading_dialog)
                dialogLoading!!.window!!.setLayout(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                dialogLoading!!.show()
            }
        } else {
            dialogLoading?.dismiss()
            dialogLoading = null
        }

    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}