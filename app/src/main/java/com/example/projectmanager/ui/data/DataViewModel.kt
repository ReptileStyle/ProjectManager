package com.example.projectmanager.ui.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {
    private val workList= exampleWorkList
    fun getDataset()=workList
}