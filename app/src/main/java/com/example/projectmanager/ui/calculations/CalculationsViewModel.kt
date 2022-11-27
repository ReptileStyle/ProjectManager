package com.example.projectmanager.ui.calculations

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectmanager.ui.data.GraphBuilder2
import com.example.projectmanager.ui.data.GraphCalculations
import com.example.projectmanager.ui.data.exampleWorkList

class CalculationsViewModel : ViewModel() {

    private val workList= exampleWorkList2
    fun getDataset()=workList

    //val myGraph = GraphBuilder2()
   // val calculator: GraphCalculations = GraphCalculations(myGraph.myEdges,myGraph.myNodes)
//    init{
//        Log.d("calcVM",myGraph.myNodes.size.toString())
//        Log.d("calcVM",calculator.nodeData.size.toString())
//        Log.d("calcVM",calculator.edgeData.size.toString())
//    }
}