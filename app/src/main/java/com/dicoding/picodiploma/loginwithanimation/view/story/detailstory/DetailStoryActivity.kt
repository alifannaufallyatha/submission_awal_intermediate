package com.dicoding.picodiploma.loginwithanimation.view.story.detailstory

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.response.GetByIdResponse
import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<DetailStoryViewModel>{
        ViewModelFactory.getInstance(this)
    }

    private var dialogLoading: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getParcelableExtra<ListStoryItem>("item") as ListStoryItem

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this@DetailStoryActivity, MainActivity::class.java))
        }

        viewModel.getByIdStory(item.id)

        viewModel.isLoading.observe(this) { showLoading(it) }

        viewModel.response.observe(this) { setupData(it) }
    }

    private fun setupData(item: GetByIdResponse) {
        Glide.with(applicationContext)
            .load(item.story.photoUrl)
            .into(binding.ivImageStory)
        binding.tvTitleStory.text = item.story.name
        binding.tvDescriptionStory.text = item.story.description
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
}