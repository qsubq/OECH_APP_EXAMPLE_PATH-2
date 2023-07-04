package com.example.oech_app_new.presentation.screen.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oech_app_new.data.model.UserModel
import com.example.oech_app_new.domain.useCase.GetUserInfoUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class HomeViewModel(private val context: Application) : AndroidViewModel(context) {
    private val getUserInfoUseCase = GetUserInfoUseCase()

    var userLiveData: MutableLiveData<UserModel> = MutableLiveData()
    var errorLiveData: MutableLiveData<String> = MutableLiveData()
    var networkLiveData: MutableLiveData<Boolean> = MutableLiveData()

//    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
//        Log.e("Maksim", "${throwable.message}")
//        errorLiveData.value = throwable.message
//    }

    fun getUserInfoForEmail(email: String) {
        if (isOnline()) {
            viewModelScope.launch() {
                Log.e("Maksim", getUserInfoUseCase.execute(email)[0].full_name)
                userLiveData.value = getUserInfoUseCase.execute(email)[0]
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
