package com.example.projectmanager.ui.data

import android.util.Log
import com.example.projectmanager.ui.renameme.toStr
import dev.bandb.graphview.graph.Node
import java.util.Locale.filter

data class NodeData(
    val node:Node,
    val dstEdges:MutableList<EdgeData>,
    val srcEdges:MutableList<EdgeData>,
    var earlyTime:Int? = null,
    var lateTime:Int? = null,
    ){
    override fun toString(): String {
        return "[node=$node,\ndstEdges=${dstEdges.joinToString(", ") { it.toString() }},\n" +
                "srcEdges=${srcEdges.joinToString(", ") { it.toString() }},\n" +
                "earlyTime=$earlyTime]"
    }
    val reservedTime:Int
    get() = lateTime!!-earlyTime!!
    var number: Int = -1
}
data class EdgeData(
    val value:Int,
    val dst:NodeData,
    val src:NodeData
){
    override fun toString(): String {
        return "[value=$value, dst=${dst.node.works.toStr()},src=${src.node.works.toStr()}]"
    }
    val reservedTime:Int
    get() = dst.lateTime!!-src.earlyTime!!-value
    val reservedTimeIndependent:Int
    get()=dst.earlyTime!!-src.lateTime!!-value
}
data class ArcInfo(
    var reservedTime:Int,
    var tension:Double
)

class GraphCalculations(val myEdges: List<MyEdge>, val nodes:List<Node>) {

    //считаем наиболее ранний срок наступления событий
    val nodeData: MutableList<NodeData> = mutableListOf()
    val edgeData: MutableList<EdgeData> = mutableListOf()
    val criticalPath: MutableList<Int> = mutableListOf()
    fun calculateEarlyTimes(){
        for (node in nodeData){
            if(node.dstEdges.isEmpty()) {
                node.earlyTime=0
                continue
            } //начальное событие
            try {
                node.earlyTime = node.dstEdges.map { it.src.earlyTime!! + it.value }.max()
            }catch (e:java.lang.Exception){
                continue //удивительный по своей дибильности, но рабочий алгоритм
            }
        }
        if(nodeData.map{it.earlyTime}.contains(null)) calculateEarlyTimes()
    }

    fun calculateLateTimes(){
        for (node in nodeData){
            if(node.srcEdges.isEmpty()) {
                node.lateTime=nodeData.map { it.earlyTime!! }.max()
                continue
            } //начальное событие
            try {
                node.lateTime = node.srcEdges.map { it.dst.lateTime!! - it.value }.min()
            }catch (e:java.lang.Exception){
                continue //удивительный по своей дибильности, но рабочий алгоритм
            }
        }
        if(nodeData.map{it.lateTime}.contains(null)) calculateLateTimes()
    }

    fun getArcInfo(vararg nums:Int):ArcInfo{
        //должна проверить, есть ли такая дуга
        if(nums[0]<0) throw java.lang.Exception("invalid argument")
        if(nums[0]>=nodeData.size) throw java.lang.Exception("invalid argument")
        for(i in 0..nums.size-2){
            if(edgeData.find { it.src==nodeData[i] && it.dst==nodeData[i+1] }==null) throw java.lang.Exception("invalid argument")
        }

    }
    fun test(){
        //GraphBuilder2().createGraph()
        nodeDataInit()
        calculateEarlyTimes()
        calculateLateTimes()
        Log.d("GC",nodeData.joinToString ("\n") {it.toString()} )
        Log.d("GC","early time :"+nodeData.map{it.earlyTime}.toString())
        Log.d("GC","late time :"+nodeData.map{it.lateTime}.toString())
    }



    //utility functions

    fun enumerateNodes(num: Int=0){
        val nodeToEnumerate = nodeData.find {
            it.number==-1 &&
            it.dstEdges.map { it.src.number }.find { it==-1 }==null
        } ?: return//находит начальное событие
        nodeToEnumerate.number=num
        nodeToEnumerate.node.data=num

        enumerateNodes(num+1)
    }



    fun nodeDataInit(){
        Log.d("GC","nodes size = "+nodes.size.toString())
        for(node in nodes){
            if(myEdges.filter { it.dst==node.works || it.src==node.works }.isNotEmpty())
                nodeData.add(NodeData(node, mutableListOf(), mutableListOf()))
        }
        migrateEdges()
        //nodeData.sortBy { it -> it.node.works.size }
        enumerateNodes()
        nodeData.sortBy { it.number }
    }
    fun migrateEdges(){
        for(myEdge in myEdges){
            val nodeSrc=nodeData.find{it.node == nodes.find { it.works==myEdge.src }!!}!!
            val nodeDst=nodeData.find{it.node == nodes.find { it.works==myEdge.dst }!!}!!
            edgeData.add(
                EdgeData(
                    myEdge.value,
                    nodeDst,
                    nodeSrc
                ).also { it->
                    nodeSrc.srcEdges.add(it)
                    nodeDst.dstEdges.add(it)
                }
            )
        }
    }


}