package com.example.oech_app_new.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.oech_app_new.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Base_Theme_OECH_APP_NEW)
        setContentView(R.layout.activity_main)
    }
}
