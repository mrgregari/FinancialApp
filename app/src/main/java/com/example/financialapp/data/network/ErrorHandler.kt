package com.example.financialapp.data.network

import com.example.financialapp.di.ApplicationScope
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@ApplicationScope
class ErrorHandler @Inject constructor(
) {
    
    fun handleException(throwable: Throwable): String {
        return when (throwable) {
            is UnknownHostException -> "Нет подключения к интернету. Проверьте соединение."
            is SocketTimeoutException -> "Превышено время ожидания ответа от сервера."
            is IOException -> "Ошибка сети. Проверьте подключение к интернету."
            is HttpException -> {
                when (throwable.code()) {
                    401 -> "Неверные учетные данные. Войдите в систему заново."
                    403 -> "Доступ запрещен."
                    404 -> "Запрашиваемые данные не найдены."
                    500 -> "Ошибка сервера. Попробуйте позже."
                    502 -> "Сервер временно недоступен."
                    503 -> "Сервис временно недоступен."
                    else -> "Ошибка сервера (${throwable.code()}). Попробуйте позже."
                }
            }
            else -> "Произошла неизвестная ошибка. Попробуйте позже."
        }
    }

}