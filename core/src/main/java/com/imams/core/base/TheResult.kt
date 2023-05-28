package com.imams.core.base

sealed class TheResult<T> {
    data class Success<T>(val data: T): TheResult<T>()
    data class Error<T>(val throwable: Throwable): TheResult<T>()
}

fun <T> Exception.toError(): TheResult<T> = TheResult.Error(Throwable(message = this.message))