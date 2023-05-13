package com.example.projectmanager.ui.util.new2

import android.util.Log
import com.example.projectmanager.ui.data.GraphBuilder2
import com.example.projectmanager.ui.data.GraphCalculations
import com.example.projectmanager.ui.renameme.Work

private const val TAG = "calcs2"

class GraphCalculations2(
    var graphCalculations: GraphCalculations,
    val graphBuilder: GraphBuilder2
) {
    val criticalPath = mutableListOf<List<Int>>()
    fun optimizeByOneDay(): Pair<Int, List<Work>> {//returns cost to optimize for 1 day and list of works to speed up
        graphCalculations.edgeData.forEach {
            Log.d("edgeData", "${it.name},${it.reservedTimeOptimization}")
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
                    if (it.name == "b3") {
                        Log.d(
                            "calcs2",
                            "${it.work.durationPessimistic!!},${it.invested},${it.work.costToSpeedUp!!},${it.work.durationPessimistic!! - it.work.invested / it.work.costToSpeedUp!!}, ${it.work.durationOptimistic}"
                        )
                    }
                    if (it.work.durationPessimistic!! - it.invested / it.work.costToSpeedUp!! != it.work.durationOptimistic && it.speedUpCost < minCost.first) {
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
            graphCalculations.edgeData.forEach {
                it.invested = map[it.work.name] ?: 0
            }
//            graphCalculations.recalculateReservedTimes(1)
            Log.d("optimization", map.toString())
//            graphCalculations.calculateEarlyTimes(1)
        }
        return map
    }

    fun getOptimizationGraphic(benefitOneDay: Int): List<PlotInfo1> {
        val list: MutableList<PlotInfo1> = mutableListOf()
        val map = firstOptimization(benefitOneDay).toMutableMap()
        graphCalculations.recalculateReservedTimes(1)
        list.add(
            PlotInfo1(
                days = graphCalculations.getEarlyTimeOfLastEvent(),
                cost = graphCalculations.getEarlyTimeOfLastEvent() * benefitOneDay + map.map { it.value }
                    .sum(),
                investmentMap = map.toMap()
            )
        )
        while (true) {
            val result = optimizeByOneDay()

            if (result.first == Int.MAX_VALUE) break
            result.second.forEach {
                if (map.containsKey(it.name)) {
                    map[it.name] = map[it.name]!! + it.costToSpeedUp!!
                } else {
                    map[it.name] = it.costToSpeedUp!!
                }
            }
            graphCalculations.edgeData.forEach {
                it.invested = map[it.work.name] ?: 0
            }
            graphCalculations.recalculateReservedTimes(1)
            list.add(
                PlotInfo1(
                    days = graphCalculations.getEarlyTimeOfLastEvent(),
                    cost = graphCalculations.getEarlyTimeOfLastEvent() * benefitOneDay + map.map { it.value }
                        .sum(),
                    investmentMap = map.toMap()
                )
            )
//            graphCalculations.recalculateReservedTimes(1)

//            graphCalculations.calculateEarlyTimes(1)
        }
        return list
    }

    fun GraphCalculations.getEarlyTimeOfLastEvent(): Int {
        return this.nodeData.maxBy { it.earlyTime ?: 0 }.earlyTime!!
    }

    init {

//        calculateAllCriticalPaths()
    }
}