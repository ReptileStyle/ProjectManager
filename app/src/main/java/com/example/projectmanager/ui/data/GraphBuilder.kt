package com.example.projectmanager.ui.data

import android.util.Log
import com.example.projectmanager.ui.renameme.Work
import dev.bandb.graphview.graph.Edge
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node
import kotlinx.coroutines.flow.merge


class GraphBuilder {

    val nodes: MutableList<Node> = mutableListOf()
    val dataset = exampleWorkList
    val myEdges:MutableList<Triple<Int,Int,Int>> = mutableListOf()//src,dst,value number
//    val g = graphBuilder{//assume dataset is sorted the right way
//        for(i in 0..dataset.size){
//            addNode("no Value","$i")
//        }
//        for(i in dataset.indices){
//            val node1=find { it -> it.id == "$i" }!! //точно есть
//            for(i in dataset[i].requiredWorksIndices()){
//                val node2=find{it.id=="$i"}!!//точно есть
//                addEdge(node1,node2,dataset[i].duration.toDouble())
//            }
//        }
//    }
    val graph = Graph()
    fun createGraph(){
        for(i in 0..dataset.size){
            nodes.add(Node("$i"))
        }
        for(i in dataset.indices){
            Log.d("GraphBuilder","${i+1} ${dataset[i].requiredWorksIndices()}")
            for(j in dataset[i].requiredWorksIndices()){
                Log.d("GraphBuilder","try nodes[${j}],nodes[${i+1}]")
                //graph.addEdge(nodes[j],nodes[i+1],i+1)
                myEdges.add(Triple(j,i+1,i+1))
            }
//            if(i==7) break
        }
//        graph.addEdge(nodes[0],nodes[1])
//        graph.addEdge(nodes[0],nodes[2])
//        graph.addEdge(nodes[0],nodes[3])
//        graph.addEdge(nodes[1],nodes[4])
//        graph.addEdge(nodes[1],nodes[5])
 //       addLastEvent()
        mergeLastEvent()
        computeCompletedWorks()
        nodes.forEach { Log.d("GraphBuilder","${it.data} - ${it.completedWorks}") }
        while (mergeEvents()){
            continue
        }
        addDummyWorks()
        myEdges.forEach { Log.d("GraphBuilder","edge - $it") }

        //merge(2,5)
        createGraph(myEdges)
    }
    //находит список работ, которые не требуются для начала других работ и соединяет их с завершающим событием
    private fun addLastEvent(){
        val asd = myEdges.map{it.first}
        val lastNodes=nodes.filter { it.data.toString().toInt() !in myEdges.map { it -> it.first } }
        nodes.add(Node("${nodes.size}"))
        lastNodes.forEach {
            //graph.addEdge(it,nodes[nodes.size-1])
            myEdges.add(Triple(nodes.indexOf(it),nodes.size-1,0))
        }
    }

    private  fun mergeLastEvent(){//все конечные вершины собираем в одну
        val lastNodes=nodes.filter { it.data.toString().toInt() !in myEdges.map { it -> it.first } }
        var lastEdges= myEdges.filter{it.second in lastNodes.map{it -> it.data.toString().toInt()}}
        myEdges.removeAll(lastEdges)
        lastEdges=lastEdges.map{it-> Triple(it.first,lastNodes[lastNodes.size-1].data.toString().toInt(),it.third) }
        myEdges.addAll(lastEdges)
    }

    private fun mergeEvents():Boolean {
        loop@ for (i in 0..dataset.size) {
            val destinationEdges = myEdges.filter { it.second == i }
            val checkedEdges: MutableList<Int> = mutableListOf()
            destinationEdges.forEach {
                if (it.third !in checkedEdges) checkedEdges.add(it.third)
                else {
                    val mergeEvent1 = it.first
                    try {
                        val mergeEvent2 = destinationEdges[checkedEdges.indexOf(it.third)].first
                        if (!myEdges.any { (it.first == mergeEvent1 && it.second == mergeEvent2) || (it.second == mergeEvent1 && it.first == mergeEvent2) }) {
                            merge(mergeEvent1, mergeEvent2)
                            return true
                        }
                        else{
                            //addDummyWorks(destinationEdges[checkedEdges.indexOf(it.third)],it)
                            //addDummyWork(it)
                            deleteRandomEdgeWhatTheFuck(destinationEdges[checkedEdges.indexOf(it.third)],it)
                            return true
                        }
                    } catch (e: java.lang.IndexOutOfBoundsException) {
                        Log.d("GraphBuilder", "exception returned")
                        return false
                    }
                }
            }
        }
        return false
    }

    private fun deleteRandomEdgeWhatTheFuck(edge1: Triple<Int, Int, Int>,edge2: Triple<Int, Int, Int>){
        myEdges.remove(edge1)
        if(isGraphCool()) return
        myEdges.add(edge1)
        myEdges.remove(edge2)
        if (isGraphCool()) return
        throw java.lang.Exception("in deleteRandomEdgeWhatTheFuck something went wrong")
    }

    private fun isGraphCool():Boolean{
        val destinations = myEdges.map { it.second }.toSet().count()
        val sources = myEdges.map { it.first }.toSet().count()
        return destinations-sources==0
    }

    private fun merge(event1:Int,event2:Int){
        var edgesToEvent2 = myEdges.filter { it.second==event2 }
        var edgesFromEvent2 = myEdges.filter { it.first==event2 }
        myEdges.removeAll(edgesToEvent2)
        myEdges.removeAll(edgesFromEvent2)
        edgesToEvent2=edgesToEvent2.map{Triple(it.first,event1,it.third)}
        edgesFromEvent2=edgesFromEvent2.map{Triple(event1,it.second,it.third)}
        myEdges.addAll(edgesToEvent2)
        myEdges.addAll(edgesFromEvent2)
        val mySet=myEdges.toSet().toMutableList()
        myEdges.removeAll { true }
        myEdges.addAll(mySet)
//        myEdges.forEach { it->
//            if(it.second==event2) {
//                myEdges.remove(it)
//                myEdges.add(Triple(it.first,event1,it.third))
//            }
//            if(it.first==event2) {
//                myEdges.remove(it)
//                myEdges.add(Triple(event1,it.second,it.third))
//            }
//        }
    }
    //добавим все фиктивные работы и события для них
    //после это будем делать мерж
    private fun addDummyWorks(edge1: Triple<Int, Int, Int>,edge2: Triple<Int, Int, Int>){
        var n = nodes.size
        nodes.add(Node("$n"))
        myEdges.remove(edge1)
        myEdges.add(Triple(edge1.first,n,0))
        myEdges.add(Triple(n,edge1.second,edge1.third))


        n = nodes.size
        nodes.add(Node("$n"))
        myEdges.remove(edge2)
        myEdges.add(Triple(edge2.first,n,edge2.third))
        myEdges.add(Triple(n,edge2.second,0))
    }

    private fun addDummyWorks(){
        val checkedEdges: MutableList<Pair<Int,Int>> = mutableListOf()
        val edgesToDummy: MutableList<Triple<Int,Int,Int>> = mutableListOf()
        myEdges.forEach { it->
            if(Pair(it.first,it.second) !in checkedEdges) checkedEdges.add(Pair(it.first,it.second))
            else{
                edgesToDummy.add(it)
            }
        }
        edgesToDummy.forEach {
            addDummyWork(it)
        }
    }

    private fun addDummyWork(edge: Triple<Int, Int, Int>){
        var n = nodes.size
        nodes.add(Node("$n"))
        myEdges.remove(edge)
        myEdges.add(Triple(edge.first,n,0))
        myEdges.add(Triple(n,edge.second,edge.third))
    }




    private fun validateEdge(edge: Triple<Int,Int,Int>){//checks is all works really completed

    }



    private fun computeCompletedWorks(){//works only if right order
        for(i in 0..dataset.size){
            val destinationEdges=myEdges.filter { it.second==i }
            for(j in destinationEdges.map { it.first }){//по всем предыдущим нодам
                nodes[i].completedWorks=nodes[i].completedWorks.plus(nodes[j].completedWorks)
            }
            nodes[i].completedWorks=nodes[i].completedWorks.plus(destinationEdges.map{it.third})//добавили доп выполненные работы
            Log.d("GraphBuilder","asd ${nodes[i].data} - ${nodes[i].completedWorks}")
        }
    }
    private fun computeCompletedWorks(edge: Triple<Int,Int,Int>){
        nodes[edge.second].completedWorks=nodes[edge.first].completedWorks.plus(edge.third)
    }


    private fun createGraph(edges: MutableList<Triple<Int,Int,Int>>){
        for(i in 0..dataset.size){
            nodes.add(Node("$i"))
        }
        edges.forEach{
            graph.addEdge(nodes[it.first],nodes[it.second],it.third)
        }
    }


    private fun Work.requiredWorksIndices():MutableList<Int>{
        val list: MutableList<Int> = mutableListOf()
        for(work in this.requiredWorks){
            list.add(dataset.indexOf(work)+1)
        }
        if(list.isEmpty()) list.add(0)//если нет требуемых работ, то добавляем начальное событие 0
        return list
    }
}