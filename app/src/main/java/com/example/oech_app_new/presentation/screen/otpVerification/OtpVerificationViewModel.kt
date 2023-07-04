package com.example.oech_app_new.presentation.screen.otpVerification

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oech_app_new.domain.useCase.OtpVerificationUseCase
import com.example.oech_app_new.domain.useCase.ResendEmailCodeUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class OtpVerificationViewModel(private val context: Application) : AndroidViewModel(context) {
    private val verificationUseCase = OtpVerificationUseCase()
    private val resendEmailCodeUseCase = ResendEmailCodeUseCase()

    var verificationLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var resendOtpLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var errorLiveData: MutableLiveData<String> = MutableLiveData()
    var networkLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("Maksim", "${throwable.message}")
        errorLiveData.value = throwable.message
    }

    fun verificationOtp(email: String, code: String) {
        if (isOnline()) {
            viewModelScope.launch(handler) {
                verificationUseCase.execute(email, code)
                verificationLiveData.value = true
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
