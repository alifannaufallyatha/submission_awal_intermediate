package com.dicoding.picodiploma.loginwithanimation.data.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
)

data class LoginRequest(
    val email: String,
    val password: String
)