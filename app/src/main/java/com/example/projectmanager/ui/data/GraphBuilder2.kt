package com.example.projectmanager.ui.data

import android.util.Log
import com.example.projectmanager.ui.renameme.Work
import com.example.projectmanager.ui.renameme.toStr
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node

data class MyEdge(var src:List<Work>,var dst:List<Work>,var value:Int, var work: Work?){
    override fun toString(): String {
        return "src=${src.toStr()} dst=${dst.toStr()} value=${value} work=${work?.name}\n"
    }
    var _valuePessimistic:Int?=null
    var _valueOptimistic:Int?=null
    val valuePessimistic:Int
        get() = if (_valuePessimistic==null) value else _valuePessimistic!!
    val valueOptimistic:Int
        get() = if (_valueOptimistic==null) value else _valueOptimistic!!
}


class GraphBuilder2 {
    val nodes: MutableList<Node> = mutableListOf()
    val dataset = exampleWorkList
    val myEdges:MutableList<MyEdge> = mutableListOf()

    var vertices=dataset.map { it.requiredWorks }.toSet().toMutableList()

    lateinit var myNodes:List<Node>


    val graph = Graph()
    fun createGraph(){
        addInitialEvents()
        processVertex()
        addListedWorks()
        addDummyWorks()
        checkForDoubleEdges()
        myNodes=createGraph(myEdges).toList()
//        Log.d("GB2","${myEdges.toStr2()}")
//        Log.d("GB2",checkForDoubleEdges().toString())
        //createGraph(myEdges)
       // GraphCalculations(myEdges, myNodes.toList()).test()
    }




    private fun addInitialEvents(){
        for(i in 0 until vertices.size){
            nodes.add(Node("",works=vertices[i]))//добавили нужное количество вершин
        }
        nodes.add(Node("",works=exampleWorkList))//финальная вершина, все работы выполнены
    }

    private fun addListedWorks(){
        for(i in dataset.indices){
            myEdges.add(MyEdge(listOf(), listOf(),0,null))//добавили все ребра, но никуда не прикрепили
        }
        var k = 0
        var i = 0
        vertices.forEach{ works->
            val edges = dataset.filter { it.requiredWorks==works }.forEach {
                myEdges[i].src=works
                myEdges[i].value=it.duration
                myEdges[i].work=it
                i++
            } //по src все норм, надо расставить dst
            k++
        }
        i=0
        for (myEdge in myEdges) {
            var minimalNode = nodes.filter { (it.works).contains(myEdge.work) }.minBy { it->
                (it.works).size
            }
            myEdge.dst=(minimalNode.works)
        }
    }

    //добаить фиктивные работы по принципу
    //проверяем все вершины на предмет того, что в них входит, если каждая из работ, то скип
    //если не все, то ищем нужные вершины и проводим линию с 0 duration
    private fun addDummyWorks(){
        for(node in nodes){
            if(node.works.size==myEdges.filter { it.dst==node.works }.size){
                continue
            }
            val thisWorks=node.works.toMutableList()
//            Log.d("GB2works","this works")
//            Log.d("GB2works",thisWorks.toStr())
//            Log.d("GB2works",(myEdges.filter { it.dst==node.works }.map { it.work } as List<Work>).toStr())
            thisWorks.removeAll(myEdges.filter { it.dst==node.works }.map { it.work })//убрали все работы, которые уже входят сюда
            myEdges.filter { it.dst==node.works }.forEach { thisWorks.removeAll(it.src) }
//            Log.d("GB2works",thisWorks.toStr())
//            Log.d("GB2works","this works")
            while(true){
                if(thisWorks.isEmpty()) break
                val currentWork=thisWorks[0]
                if(currentWork.name=="dummyWork") break
               // Log.d("GB2works","current work="+currentWork.name)
                //ищем вершину, где максимальное количество работ, включающее currentWork, но меньше, чем у текущей вершины
                val maxNode = nodes.filter { it.works.contains(currentWork) && it.works.size<node.works.size }.maxBy { it.works.size }
               // Log.d("GB2works","maxNode="+maxNode.works.toStr())
                exampleWorkList.add(Work("dummyWork",0,maxNode.works))
                myEdges.add(MyEdge(maxNode.works,node.works,0, exampleWorkList.last()))
              // Log.d("GB22",nodes.filter { it.works.contains(currentWork) && it.works.size<node.works.size }.toString())
                thisWorks.remove(currentWork)
                thisWorks.removeAll(maxNode.works)
            }
        }
    }
    fun List<Work>.toStr():String{
        return this.joinToString ( ", " ){it.name}
    }
    fun MutableList<MyEdge>.toStr2():String{
        return this.joinToString ( ", " ){it.toString()}
    }
    private fun checkForDoubleEdges(){
        for(i in myEdges.indices){
            for(j in i+1 until myEdges.size){
                if(myEdges[i].dst==myEdges[j].dst && myEdges[i].src==myEdges[j].src){
                    nodes.add(Node("",works = myEdges[i].src.plus(myEdges[i].work!!).toList()))
                    myEdges.add(MyEdge(myEdges[j].src,nodes.last().works,0,
                        Work("dummyWork",0,myEdges[j].src)))
                    myEdges[j].src=nodes.last().works
                   // Log.d("GB32","1111")
                }
            }
        }
    }

    private fun createGraph(edges: MutableList<MyEdge>):Set<Node>{
        var myNodes = setOf<Node>()
        edges.forEach { edge->
            val nodesrc=nodes.find { it.works == edge.src }!!
            val nodedst=nodes.find { it.works == edge.dst }!!//also filter out nodes
            myNodes=myNodes.plus(nodedst)
            myNodes=myNodes.plus(nodesrc)
            graph.addEdge(nodesrc,nodedst,edge.value)
        }
        return myNodes
    }

    // превращаем все моменты типа (b4,b7),(b5,b7) в b7,(b4,b7),(b5,b7) для корректной обработки далее
    //
    private fun processVertex(){
        for(i in exampleWorkList.indices){
            for(j in i until exampleWorkList.size){//ищем пересечения множеств, если оно не равно 0 и его нет, то добавляем событие
                val intersect= exampleWorkList[i].requiredWorks.intersect(exampleWorkList[j].requiredWorks)
                if (intersect.isNotEmpty()){
                    if(exampleWorkList.filter { it.requiredWorks==intersect }.isEmpty()){
                        nodes.add(Node("",works=intersect.toList()))
                    }
                }
            }
        }
    }
}