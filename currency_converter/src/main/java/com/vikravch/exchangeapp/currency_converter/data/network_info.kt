package com.vikravch.exchangeapp.currency_converter.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.Keep
import com.google.gson.Gson
import retrofit2.HttpException

interface NetworkInfo {
    fun isConnected(): Boolean
}

class NetworkInfoImpl(private val context: Context) : NetworkInfo {
    override fun isConnected(): Boolean {
        var isConnected = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            isConnected = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            isConnected = when (connectivityManager.activeNetworkInfo?.type) {
                ConnectivityManager.TYPE_WIFI -> true
                ConnectivityManager.TYPE_MOBILE -> true
                ConnectivityManager.TYPE_ETHERNET -> true
                else -> false
            }
        }
        return isConnected
    }
}

@Keep
class UserNotFoundException : Exception("User not found")
@Keep
class NoInternetException : Exception("No internet connection")
@Keep
class ServerException(message: String) : Exception(message)
@Keep
class WrongRequestException(message: String) : Exception(message)

@Keep
data class ServerError(
    val code: Int,
    val message: String
)

@Keep
fun HttpException.toServerError(): ServerError {
    val gson = Gson()
    return gson.fromJson(this.response()?.errorBody()?.charStream(), ServerError::class.java)
}