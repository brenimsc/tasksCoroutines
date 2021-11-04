package com.example.tasks

import java.lang.Exception

sealed class ResultRepository<out R> {
    data class Sucess<out T>(val dado: T?) : ResultRepository<T?>()
    data class Error(val exception: Exception) : ResultRepository<Nothing>()
}