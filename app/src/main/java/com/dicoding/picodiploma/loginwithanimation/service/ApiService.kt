package com.dicoding.picodiploma.loginwithanimation.service

import com.dicoding.picodiploma.loginwithanimation.data.request.LoginRequest
import com.dicoding.picodiploma.loginwithanimation.data.request.RegisterRequest
import com.dicoding.picodiploma.loginwithanimation.response.CreateResponse
import com.dicoding.picodiploma.loginwithanimation.response.GetAllStory
import com.dicoding.picodiploma.loginwithanimation.response.GetByIdResponse
import com.dicoding.picodiploma.loginwithanimation.response.GuestResponse
import com.dicoding.picodiploma.loginwithanimation.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST ("/v1/register")
    fun register(
        @Body post:RegisterRequest,
    ): Call<RegisterResponse>

    @POST ("/v1/login")
    fun login (
        @Body post: LoginRequest,
    ):Call<LoginResponse>
    @GET("/v1/stories")
    fun getAll(
        @Header("Authorization") token: String
    ): Call<GetAllStory>

    @GET("/v1/stories/{id}")
    fun getById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<GetByIdResponse>

    @Multipart
    @POST("/v1/stories")
    fun createStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): Call<CreateResponse>

    @Multipart
    @POST("/v1/stories/guest")
    fun createStoryGuest(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): Call<GuestResponse>
}