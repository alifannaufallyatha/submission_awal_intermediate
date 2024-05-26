package com.dicoding.picodiploma.loginwithanimation.view.auth.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.config.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.request.RegisterRequest
import com.dicoding.picodiploma.loginwithanimation.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    private val _response = MutableLiveData<RegisterResponse?>()
    val response: LiveData<RegisterResponse?> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        val request = RegisterRequest(name, email, password)
        val client = ApiConfig.getApiService().register(request)

        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _response.value = response.body()
                } else {
                    _errorMessage.value = "Registration failed: ${response.message()}"
                    Log.e("Register User", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Registration failed: ${t.message.toString()}"
                Log.e("Register User", "Failure: ${t.message.toString()}")
            }
        })
    }
}
