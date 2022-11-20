package com.example.projectmanager.ui.data

import android.util.Log
import com.example.projectmanager.ui.renameme.Work
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node


class GraphBuilder {

    val nodes: MutableList<Node> = mutableListOf()
    val dataset = exampleWorkList
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
                graph.addEdge(nodes[j],nodes[i+1])
            }
//            if(i==7) break
        }
//        graph.addEdge(nodes[0],nodes[1])
//        graph.addEdge(nodes[0],nodes[2])
//        graph.addEdge(nodes[0],nodes[3])
//        graph.addEdge(nodes[1],nodes[4])
//        graph.addEdge(nodes[1],nodes[5])
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