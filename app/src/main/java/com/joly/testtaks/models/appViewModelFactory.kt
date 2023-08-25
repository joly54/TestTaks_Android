package com.joly.testtaks.models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class appViewModelFactory (
        private val application: Application
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(dataBaseViewModel::class.java)) {
            return dataBaseViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}