package com.example.projectmanager.ui.util.new

import android.util.Log
import com.example.projectmanager.ui.data.GraphBuilder2
import com.example.projectmanager.ui.data.GraphCalculations
import com.example.projectmanager.ui.renameme.Work

class GraphCalculations2(val graphCalculations: GraphCalculations,val graphBuilder: GraphBuilder2) {
    val criticalPath = mutableListOf<List<Int>>()
    fun calculateAllCriticalPaths(){
        val edgesNoReservedTime = graphCalculations.edgeData.filter { it.reservedTime==0 }
        Log.d(TAG,edgesNoReservedTime.joinToString { it.name })
    }
    fun firstOptimization():List<Work>{
        TODO()
    }

    init {
        calculateAllCriticalPaths()
    }
}
private const val TAG = "Calculating"