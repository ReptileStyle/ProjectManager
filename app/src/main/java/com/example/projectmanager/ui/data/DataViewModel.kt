package com.example.projectmanager.ui.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectmanager.ui.renameme.Work
import dev.bandb.graphview.util.VectorF

class DataViewModel : ViewModel() {
    var myGraph:GraphBuilder2
    var calculator:GraphCalculations
    var myMovements:MutableList<VectorF> = mutableListOf()

    private val workList= exampleWorkList.toMutableList()
    fun getDataset()=workList
    fun addWork(work: Work){
        Log.d("click","addwork")
        workList.removeAll { it.name=="dummyWork" }
        workList.add(work)
        myGraph = GraphBuilder2(workList,currentMode)
        calculator = GraphCalculations(myGraph.myEdges,myGraph.myNodes,currentMode)
        for (i in myGraph.graph.nodes.indices) {
            clearMovements()
            myGraph.graph.nodes[i].moveY=500f
            myGraph.graph.nodes[i].moveX=500f
        }
    }
    fun removeWork(name:String){
        Log.d("click","remove work")
        workList.remove(workList.find { it.name==name })
        myGraph = GraphBuilder2(workList,currentMode)
        calculator = GraphCalculations(myGraph.myEdges,myGraph.myNodes,currentMode)
        for (i in myGraph.graph.nodes.indices) {
            clearMovements()
            myGraph.graph.nodes[i].moveY=500f
            myGraph.graph.nodes[i].moveX=500f
        }
    }
    var currentMode=1
    fun changeMode(mode: Int){
        Log.d("click","change mode")
        currentMode=mode
        myGraph = GraphBuilder2(workList,currentMode)
        calculator = GraphCalculations(myGraph.myEdges,myGraph.myNodes,currentMode)
        for (i in myGraph.graph.nodes.indices) {
            myGraph.graph.nodes[i].moveY=500f+myMovements[i].x
            myGraph.graph.nodes[i].moveX=500f+myMovements[i].y
        }
    }

    init{
        Log.d("dataViewModel","init")
        Log.d("click","init")
        myGraph = GraphBuilder2(workList,currentMode)
        calculator = GraphCalculations(myGraph.myEdges,myGraph.myNodes,currentMode)

        for (i in myGraph.graph.nodes.indices) {
            clearMovements()
            myGraph.graph.nodes[i].moveY=500f
            myGraph.graph.nodes[i].moveX=500f
        }
    }
    fun clearMovements(){
        myMovements.clear()
        myGraph.graph.nodes.forEach { _ -> myMovements.add(VectorF(0f,0f)) }
    }
}
