package com.dicoding.picodiploma.loginwithanimation.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.config.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.response.GetAllStory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _response = MutableLiveData<GetAllStory>()
    val response: LiveData<GetAllStory> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    init {
        viewModelScope.launch {
            repository.getToken().asLiveData().observeForever {
                Log.d("OBSERVE TOKEN", it)
                _token.value = it
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getAllStory() {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getToken().asLiveData().observeForever {
                Log.d("OBSERVE TOKEN", it)
                if (it != "" && it != null) {
                    val client = ApiConfig.getApiService().getAll("Bearer $it")

                    client.enqueue(object : Callback<GetAllStory> {
                        override fun onResponse(
                            call: Call<GetAllStory>,
                            response: Response<GetAllStory>
                        ) {
                            _isLoading.value = false
                            if (response.isSuccessful) {
                                _response.value = response.body()
                            } else {
                                Log.e("Get All Story", "On failure: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<GetAllStory>, t: Throwable) {
                            _isLoading.value = false
                            Log.e("Get All Story", "onFailure: ${t.message.toString()}")
                        }
                    })
                } else {
                    _isLoading.value = false
                }
            }
        }
    }

}