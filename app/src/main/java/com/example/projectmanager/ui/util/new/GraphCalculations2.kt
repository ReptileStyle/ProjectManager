package com.example.projectmanager.ui.util.new

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.projectmanager.ui.data.GraphBuilder2
import com.example.projectmanager.ui.data.GraphCalculations
import com.example.projectmanager.ui.renameme.Work
import kotlin.math.min

class GraphCalculations2(
    var graphCalculations: GraphCalculations,
    val graphBuilder: GraphBuilder2
) {
    val criticalPath = mutableListOf<List<Int>>()
    fun optimizeByOneDay(): Pair<Int, List<Work>> {//returns cost to optimize for 1 day and list of works to speed up
        graphCalculations.edgeData.forEach {
            Log.d("edgeData","${it.name},${it.reservedTimeOptimization}")
        }
        val edgesNoReservedTime =
            graphCalculations.edgeData.filter { it.reservedTimeOptimization == 0 }.toMutableList()
        Log.d(TAG, edgesNoReservedTime.joinToString { it.name })
        val finishId = edgesNoReservedTime.maxBy { it.dstId }.dstId
        fun getMinimumCostToSpeedUp(vertexId: Int): Pair<Int, List<Work>> {
            Log.d("calcs2", "enter $vertexId")
            val edgesWithDstInVertexId = edgesNoReservedTime.filter { it.dstId == vertexId }
            edgesNoReservedTime.removeAll { it.dstId == vertexId }
            if (edgesWithDstInVertexId.isEmpty()) {
                return Pair(Int.MAX_VALUE, listOf()) //cant speed up start event
            } else {
                var sum = 0
                val works: MutableList<Work> = mutableListOf()
                edgesWithDstInVertexId.forEach {
                    val minCost = getMinimumCostToSpeedUp(it.srcId)
                    if(it.name=="b3"){
                        Log.d("calcs2","${it.work.durationPessimistic!!},${it.invested},${it.work.costToSpeedUp!!},${it.work.durationPessimistic!! - it.work.invested/it.work.costToSpeedUp!!}, ${it.work.durationOptimistic}")
                    }
                    if (it.work.durationPessimistic!! - it.invested/it.work.costToSpeedUp!! != it.work.durationOptimistic && it.speedUpCost < minCost.first) {
                        sum += it.speedUpCost
                        works.add(it.work)
                    } else {
                        sum += minCost.first
                        works.addAll(minCost.second)
                    }

                }

                return Pair(sum, works)
            }
        }

        val result = getMinimumCostToSpeedUp(finishId)
        Log.d("calcs2", "result ${result.first},${result.second.joinToString { it.name }}")
        return Pair(result.first, result.second)
    }


    fun firstOptimization(benefitOneDay: Int): Map<String, Int> {//returns investment map
        val map: MutableMap<String, Int> = mutableMapOf()
        while (true) {
            graphCalculations.recalculateReservedTimes(1)
            val result = optimizeByOneDay()
            if (result.first > benefitOneDay) break
            result.second.forEach {
                if (map.containsKey(it.name)) {
                    map[it.name] = map[it.name]!! + it.costToSpeedUp!!
                } else {
                    map[it.name] = it.costToSpeedUp!!
                }
            }
            graphCalculations.edgeData.forEach{
                it.invested=map[it.work.name] ?: 0
            }
//            graphCalculations.recalculateReservedTimes(1)
            Log.d("optimization",map.toString())
//            graphCalculations.calculateEarlyTimes(1)
        }
        return map
    }


    init {

//        calculateAllCriticalPaths()
    }
}

private const val TAG = "Calculating"