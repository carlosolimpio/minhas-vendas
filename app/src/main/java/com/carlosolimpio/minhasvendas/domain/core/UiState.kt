package com.carlosolimpio.minhasvendas.domain.core

sealed class UiState<out T : Any> {
    data class Success<out T : Any>(val data: T) : UiState<T>()
    data class NotFound(val message: String) : UiState<Nothing>()
    data class Error(val error: Throwable) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
}
