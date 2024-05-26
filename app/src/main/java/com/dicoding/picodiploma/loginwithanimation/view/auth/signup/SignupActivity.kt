package com.dicoding.picodiploma.loginwithanimation.view.auth.signup

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        binding.signupButton.setOnClickListener{
            validation()
        }

        viewModel.response.observe(this){
            AlertDialog.Builder(this).apply {
                setTitle("Done!")
                setMessage(it?.message)
                setPositiveButton("Lanjut") { _, _ ->
                    finish()
                }
                create()
                show()
            }
        }

        viewModel.isLoading.observe(this){isLoading ->
            if (isLoading){
                binding.progresbarSignup.visibility = View.VISIBLE
            }else{
                binding.progresbarSignup.visibility = View.GONE
            }

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

    private fun validation(){
        binding.apply {
            val email = binding.emailEditTextLayout.editText?.text.toString()
            val password = binding.passwordEditTextLayout.editText?.text.toString()
            val name = binding.nameEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()){
                register(name, email, password)
            }  else {
                Toast.makeText(
                    this@SignupActivity,
                    "Please Filled All of Column",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun register(name: String, email: String, password: String) {

        viewModel.apply {
            register(name, email, password)
        }

    }
}