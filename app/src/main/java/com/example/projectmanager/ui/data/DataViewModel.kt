package com.example.projectmanager.ui.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectmanager.ui.renameme.Work

class DataViewModel : ViewModel() {
    var myGraph:GraphBuilder2
    var calculator:GraphCalculations

    private val workList= exampleWorkList.toMutableList()
    fun getDataset()=workList
    fun addWork(work: Work){
        workList.removeAll { it.name=="dummyWork" }
        workList.add(work)
        myGraph = GraphBuilder2(workList)
        calculator = GraphCalculations(myGraph.myEdges,myGraph.myNodes)
    }
    fun removeWork(name:String){
        workList.remove(workList.find { it.name==name })
        myGraph = GraphBuilder2(workList)
        calculator = GraphCalculations(myGraph.myEdges,myGraph.myNodes)
    }
    var currentMode=1

    init{
        Log.d("dataViewModel","init")
        myGraph = GraphBuilder2(workList)
        calculator = GraphCalculations(myGraph.myEdges,myGraph.myNodes)
    }
}