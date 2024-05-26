package com.dicoding.picodiploma.loginwithanimation.view.story.detailstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.config.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.response.GetByIdResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel(private val repository: UserRepository): ViewModel() {

    private val _response = MutableLiveData<GetByIdResponse>()
    val response: LiveData<GetByIdResponse> = _response

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

    fun getByIdStory(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getToken().asLiveData().observeForever {
                val client = ApiConfig.getApiService().getById("Bearer $it", id)

                client.enqueue(object : Callback<GetByIdResponse> {
                    override fun onResponse(
                        call: Call<GetByIdResponse>,
                        response: Response<GetByIdResponse>
                    ) {
                        _isLoading.value = false

                        if (response.isSuccessful) _response.value = response.body()
                        else Log.e("Detail Story", "onFailure: ${response.message()}")
                    }

                    override fun onFailure(call: Call<GetByIdResponse>, t: Throwable) {
                        _isLoading.value = false
                        Log.e("Detail Story", "onFailure: ${t.message.toString()}")
                    }
                })
            }
        }
    }
}