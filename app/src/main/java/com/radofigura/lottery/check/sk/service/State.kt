package com.radofigura.lottery.check.sk.service

import java.lang.Exception

sealed class State<out T> {
    object LoadingState: State<Nothing>()
    data class ErrorState(var exception: Throwable): State<Nothing>()
    data class DataState<T>(var data: T): State<T>()
}
