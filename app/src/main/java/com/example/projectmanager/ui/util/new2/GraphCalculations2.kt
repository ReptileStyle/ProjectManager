package com.example.projectmanager.ui.util.new2

import android.util.Log
import com.example.projectmanager.ui.data.EdgeData
import com.example.projectmanager.ui.data.GraphBuilder2
import com.example.projectmanager.ui.data.GraphCalculations
import com.example.projectmanager.ui.renameme.Work
import dev.bandb.graphview.graph.Edge
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node
import org.jgrapht.alg.shortestpath.AllDirectedPaths
import org.jgrapht.graph.DefaultDirectedWeightedGraph
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleDirectedGraph
import org.jgrapht.graph.SimpleDirectedWeightedGraph
import java.lang.Math.min

private const val TAG = "calcs2"

class GraphCalculations2(
    var graphCalculations: GraphCalculations,
    val graphBuilder: GraphBuilder2
) {
    val criticalPath = mutableListOf<List<Int>>()
    fun optimizeByOneDay(): Pair<Int, List<Work>> {//returns cost to optimize for 1 day and list of works to speed up
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

            Log.d(
                "crits",
                "${graphCalculations.getEarlyTimeOfLastEvent()} \n ${
                    graphCalculations.getAllCriticalPaths().toString()
                }"
            )
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

    fun GraphCalculations.getAllCriticalPaths(): List<List<Pair<Int, Int>>> {
        val lastEvent = this.nodeData.maxBy { it.earlyTime ?: 0 }
        fun helperFun(edgeData: EdgeData): List<List<Pair<Int, Int>>> {
            if (edgeData.src.dstEdges.isEmpty()) return listOf(
                listOf(
                    Pair(
                        edgeData.src.number,
                        edgeData.dst.number
                    )
                )
            ) //return if first event
            val list: MutableList<List<Pair<Int, Int>>> = mutableListOf()
            edgeData.src.dstEdges.filter { it.reservedTimeOptimization == 0 }.forEach {
                list.addAll(helperFun(it).map {
                    it.plus(
                        Pair(
                            edgeData.src.number,
                            edgeData.dst.number
                        )
                    )
                })
            }
            return list
        }

        val list: MutableList<List<Pair<Int, Int>>> = mutableListOf()
        lastEvent.dstEdges.filter { it.reservedTimeOptimization == 0 }.forEach {
            list.addAll(helperFun(it))
        }
        return list
    }

    fun GraphCalculations.getCriticalPathsGraph(): SimpleDirectedWeightedGraph<Int, DefaultWeightedEdge> {
        graphCalculations.recalculateReservedTimes(1)
        val graph =
            SimpleDirectedWeightedGraph<Int, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)
        val lastEvent = this.nodeData.maxBy { it.earlyTime ?: 0 }
        graphCalculations.nodeData.forEach {
            graph.addVertex(it.number)
        }
        graphCalculations.edgeData.filter { it.reservedTimeOptimization == 0 }
            .forEach { edge ->
                val weightedEdge = DefaultWeightedEdge()
                graph.addEdge(edge.src.number, edge.dst.number, weightedEdge)
                Log.d("newgraphcalc","${edge.work.name},${edge.invested},${edge.work.durationPessimistic!! - edge.invested / edge.work.costToSpeedUp!! != edge.work.durationOptimistic} ")
                graph.setEdgeWeight(
                    weightedEdge,
                    if (edge.work.durationPessimistic!! - edge.invested / edge.work.costToSpeedUp!! != edge.work.durationOptimistic)
                        edge.speedUpCost.toDouble() else Int.MAX_VALUE.toDouble(),
                )
            }
        return graph
    }

    fun getOptimizationGraphic2(benefitOneDay: Int):List<PlotInfo1> {
        val list: MutableList<PlotInfo1> = mutableListOf()
        val map = mutableMapOf<String,Int>() //save optimization config
        var criticalGraph = graphCalculations.getCriticalPathsGraph()
        val startId = criticalGraph.vertexSet().minBy { it }
        val finishId = criticalGraph.vertexSet().maxBy { it }
        Log.d("newgraphcalc", criticalGraph.toString())
        var allPaths =
            AllDirectedPaths(criticalGraph).getAllPaths(
                startId,
                finishId,
                true,
                Int.MAX_VALUE
            )
        val edgesToCheck: MutableSet<DefaultWeightedEdge> = mutableSetOf()
        val minimumCost = Int.MAX_VALUE
        allPaths.forEach {
            edgesToCheck.addAll(it.edgeList)
        }//create default set of edges to check
        fun getMinimalCostEdgeList(localEdgesToCheck: Set<DefaultWeightedEdge>): List<DefaultWeightedEdge> {
            val edgeList: MutableList<List<DefaultWeightedEdge>> = mutableListOf()
            for (edge in localEdgesToCheck) {
                val remainingEdgesToCheck = localEdgesToCheck.toMutableSet()
                for (path in allPaths) {
                    if (remainingEdgesToCheck.isEmpty()) break
                    if (path.edgeList.contains(edge)) {
                        remainingEdgesToCheck.removeAll(path.edgeList.toSet())
                    }
                }
                edgeList.add(
                    getMinimalCostEdgeList(remainingEdgesToCheck).plus(
                        edge
                    )
                )
            }
            return edgeList.minByOrNull {
                var sum = 0
                for(edge in it){
                    if( criticalGraph.getEdgeWeight(edge).toInt() == Int.MAX_VALUE){
                        sum = Int.MAX_VALUE
                        break
                    }
                    sum += criticalGraph.getEdgeWeight(edge).toInt()
                }
                sum
            } ?: listOf()
        }
        while (true){
            val result = getMinimalCostEdgeList(edgesToCheck)
            if(result.sumOf { criticalGraph.getEdgeWeight(it) } >= Int.MAX_VALUE || result.isEmpty()) break
            //apply changes to graph
            result.forEach {
                val src = criticalGraph.getEdgeSource(it)
                val dst = criticalGraph.getEdgeTarget(it)
                val name = graphCalculations.edgeData.find { it.src.number==src && it.dst.number==dst }!!.work.name
                if(map.containsKey(name)){
                    map[name] = map[name]!! + criticalGraph.getEdgeWeight(it).toInt()
                }else{
                    map[name] = criticalGraph.getEdgeWeight(it).toInt()
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
            Log.d("newgraphcalc",map.toString())
            criticalGraph = graphCalculations.getCriticalPathsGraph()
            allPaths =
                AllDirectedPaths(criticalGraph).getAllPaths(
                    startId,
                    finishId,
                    true,
                    Int.MAX_VALUE
                )
            edgesToCheck.clear()
            criticalGraph.edgeSet().forEach {
                if(criticalGraph.getEdgeWeight(it).toInt()<Int.MAX_VALUE)
                    edgesToCheck.add(it)
            }
        }
        return list
    }

    init {

//        calculateAllCriticalPaths()
    }
}