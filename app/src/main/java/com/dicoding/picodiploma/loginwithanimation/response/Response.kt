package com.dicoding.picodiploma.loginwithanimation.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class LoginResponse(

    @field:SerializedName("loginResult")
    val loginResult: LoginResult,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)

data class LoginResult(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("token")
    val token: String,
)

data class RegisterResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)

data class GetAllStory(
    val listStory: List<ListStoryItem>,
    val error: Boolean,
    val message: String
)

@Parcelize
data class ListStoryItem(
    val photoUrl: String,
    val createdAt: String,
    val name: String,
    val description: String,
    val id: String,
) : Parcelable

data class CreateResponse(
    val error: Boolean,
    val message: String
)

data class GetByIdResponse(
    val error: Boolean,
    val message: String,
    val story: Story
)

data class Story(
    val photoUrl: String,
    val createdAt: String,
    val name: String,
    val description: String,
    val lon: Any,
    val id: String,
    val lat: Any
)

data class GuestResponse(
    val error: Boolean,
    val message: String
)