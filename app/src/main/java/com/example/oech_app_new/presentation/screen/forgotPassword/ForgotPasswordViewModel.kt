package com.example.oech_app_new.presentation.screen.forgotPassword

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oech_app_new.domain.useCase.ResendEmailCodeUseCase
import com.example.oech_app_new.domain.useCase.SendOtpCodeUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(private val context: Application) : AndroidViewModel(context) {
    private val sendOtpCodeUseCase = SendOtpCodeUseCase()
    private val resendEmailCodeUseCase = ResendEmailCodeUseCase()

    var sendOtpLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var resendOtpLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var errorLiveData: MutableLiveData<String> = MutableLiveData()
    var networkLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("Maksim", "${throwable.message}")
        errorLiveData.value = throwable.message
    }

    fun sendOtp(email: String) {
        if (isOnline()) {
            viewModelScope.launch(handler) {
                sendOtpCodeUseCase.execute(email)
                sendOtpLiveData.value = true
            }
        } else {
            networkLiveData.value = false
        }
    }

    fun resendOtp(email: String) {
        if (isOnline()) {
            viewModelScope.launch(handler) {
                resendEmailCodeUseCase.execute(email)
                resendOtpLiveData.value = true
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
