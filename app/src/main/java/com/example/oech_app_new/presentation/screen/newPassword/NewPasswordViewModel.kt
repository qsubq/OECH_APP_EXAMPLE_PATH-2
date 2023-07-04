package com.example.oech_app_new.presentation.screen.newPassword

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oech_app_new.data.model.UserModel
import com.example.oech_app_new.domain.useCase.ChangePasswordUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class NewPasswordViewModel(private val context: Application) : AndroidViewModel(context) {

    var changePasswordUseCase = ChangePasswordUseCase()
    var changePasswordLiveData: MutableLiveData<UserModel> = MutableLiveData()
    var errorLiveData: MutableLiveData<String> = MutableLiveData()
    var networkLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("Maksim", "${throwable.message}")
        errorLiveData.value = throwable.message
    }

    fun changePassword(password: String, email: String) {
        if (isOnline()) {
            viewModelScope.launch(handler) {
                changePasswordLiveData.value = changePasswordUseCase.execute(password, email)[0]
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
