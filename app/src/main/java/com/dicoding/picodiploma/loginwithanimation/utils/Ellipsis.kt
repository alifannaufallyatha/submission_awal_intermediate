package com.meone.storyapp.utils

fun String.addEllipsisAfter10Words(): String {
    val words = this.split(" ")
    val first10Words = if (words.size > 10) words.subList(0, 10) else words
    return if (words.size > 10) "${first10Words.joinToString(" ")}..." else this
}