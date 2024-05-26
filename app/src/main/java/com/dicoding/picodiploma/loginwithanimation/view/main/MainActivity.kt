package com.dicoding.picodiploma.loginwithanimation.view.main

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.adapter.ListStoryAdapter
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.story.create.CreateStoryActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import com.meone.storyapp.utils.SpaceItemDecoration

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private var dialogLoading: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        viewModel.getAllStory()

        viewModel.isLoading.observe(this) { showLoading(it) }

        viewModel.response.observe(this) {
            setListStory(it.listStory)
        }

        setupView()

        binding.createBtn.setOnClickListener {
            startActivity(Intent(this, CreateStoryActivity::class.java))
        }

        binding.logoutBtn.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setListStory(item: List<ListStoryItem>){
        if (item.isNotEmpty()) {
            val adapter = ListStoryAdapter(item)
            binding.rvListStory.layoutManager = LinearLayoutManager(this)
            binding.rvListStory.addItemDecoration(SpaceItemDecoration(16))
            binding.rvListStory.adapter = adapter

        }

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