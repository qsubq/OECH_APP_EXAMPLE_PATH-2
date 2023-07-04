package com.example.oech_app_new.presentation.screen.signIn

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oech_app_new.domain.useCase.SignInUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class SignInViewModel(private val context: Application) : AndroidViewModel(context) {
    private val signInUseCase = SignInUseCase()

    var signInLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var errorLiveData: MutableLiveData<String> = MutableLiveData()
    var networkLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("Maksim", "${throwable.message}")
        errorLiveData.value = throwable.message
    }

    fun signIn(email: String, password: String) {
        if (isOnline()) {
            viewModelScope.launch(handler) {
                signInUseCase.execute(email, password)
                signInLiveData.value = true
            }
        } else {
            networkLiveData.value = false
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities != null
    }
}
