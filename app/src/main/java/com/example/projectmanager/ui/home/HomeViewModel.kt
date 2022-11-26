package com.example.projectmanager.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectmanager.ui.data.GraphBuilder2
import com.example.projectmanager.ui.data.GraphCalculations

class HomeViewModel : ViewModel() {
    var myGraph:GraphBuilder2
    var calculator:GraphCalculations

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    init{
        myGraph = GraphBuilder2()
        calculator = GraphCalculations(myGraph.myEdges,myGraph.myNodes)
    }
}