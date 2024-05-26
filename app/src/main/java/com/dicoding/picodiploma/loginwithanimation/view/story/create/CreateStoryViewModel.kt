package com.dicoding.picodiploma.loginwithanimation.view.story.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.config.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.response.CreateResponse
import com.dicoding.picodiploma.loginwithanimation.response.GuestResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CreateStoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _response = MutableLiveData<CreateResponse>()
    val response: LiveData<CreateResponse> = _response

    private val _guestResponse = MutableLiveData<GuestResponse>()
    val guestResponse: LiveData<GuestResponse> = _guestResponse

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

    fun createStory(imageFile: File, description: String) {
        _isLoading.value = true
        val reqImage = imageFile.asRequestBody("image/*".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("photo", imageFile.name, reqImage)
        viewModelScope.launch {
            repository.getToken().asLiveData().observeForever {
                if (it.isEmpty()) {
                    val client = ApiConfig.getApiService().createStoryGuest(multipartBody, description.toRequestBody("text/plain".toMediaType()))

                    client.enqueue(object : Callback<GuestResponse> {
                        override fun onResponse(
                            call: Call<GuestResponse>,
                            response: Response<GuestResponse>
                        ) {
                            _isLoading.value = false
                            if (response.isSuccessful) {
                                _guestResponse.value = response.body()
                            } else {
                                Log.e("Create Story", "onFailure ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<GuestResponse>, t: Throwable) {
                            _isLoading.value = false
                            Log.e("Create Story", "onFailure ${t.message.toString()}")
                        }
                    })
                } else {
                    val client = ApiConfig.getApiService().createStory("Bearer ${it}", multipartBody, description.toRequestBody("text/plain".toMediaType()))

                    client.enqueue(object : Callback<CreateResponse> {
                        override fun onResponse(
                            call: Call<CreateResponse>,
                            response: Response<CreateResponse>
                        ) {
                            _isLoading.value = false
                            if (response.isSuccessful) {
                                _response.value = response.body()
                            } else {
                                Log.e("Create Story", "onFailure ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<CreateResponse>, t: Throwable) {
                            _isLoading.value = false
                            Log.e("Create Story", "onFailure ${t.message.toString()}")
                        }
                    })
                }

            }
        }
    }

}