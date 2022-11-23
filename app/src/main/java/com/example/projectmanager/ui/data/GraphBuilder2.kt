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
}


class GraphBuilder2 {
    val nodes: MutableList<Node> = mutableListOf()
    val dataset = exampleWorkList
    val myEdges:MutableList<MyEdge> = mutableListOf()

    var vertices=dataset.map { it.requiredWorks }.toSet().toMutableList()


    val graph = Graph()
    fun createGraph(){

//        graph.addEdge(nodes[0],nodes[1])
//        graph.addEdge(nodes[0],nodes[2])
//        graph.addEdge(nodes[0],nodes[3])
//        graph.addEdge(nodes[1],nodes[4])
//        graph.addEdge(nodes[1],nodes[5])
        //       addLastEvent()

        //merge(2,5)
        for(i in 0 until vertices.size){
            nodes.add(Node("",works=vertices[i]))//добавили нужное количество вершин
        }
        nodes.add(Node("",works=exampleWorkList))//финальная вершина, все работы выполнены
        processVertex()
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
//        vertices.forEach { works->
//            var minimalNode = nodes.minBy { it->
//                (it.works).size
//                }
//
//            myEdges[i].dst//расставить индексы как надо
//        }
        for (myEdge in myEdges) {
            var minimalNode = nodes.filter { (it.works).contains(myEdge.work) }.minBy { it->
                (it.works).size
            }
            myEdge.dst=(minimalNode.works)
        }
        //добаить фиктивные работы по принципу
        //проверяем все вершины на предмет того, что в них входит, если каждая из работ, то скип
        //если не все, то ищем нужные вершины и проводим линию с 0 duration
        for(node in nodes){
            if(node.works.size==myEdges.filter { it.dst==node.works }.size){
                continue
            }
            val thisWorks=node.works.toMutableList()
            Log.d("GB2works","this works")
            Log.d("GB2works",thisWorks.toStr())
            Log.d("GB2works",(myEdges.filter { it.dst==node.works }.map { it.work } as List<Work>).toStr())


            thisWorks.removeAll(myEdges.filter { it.dst==node.works }.map { it.work })//убрали все работы, которые уже входят сюда
            myEdges.filter { it.dst==node.works }.forEach { thisWorks.removeAll(it.src) }
            Log.d("GB2works",thisWorks.toStr())
            Log.d("GB2works","this works")
            while(true){
                if(thisWorks.isEmpty()) break
                val currentWork=thisWorks[0]
                if(currentWork.name=="dummyWork") break
                Log.d("GB2works","current work="+currentWork.name)
                //ищем вершину, где максимальное количество работ, включающее currentWork, но меньше, чем у текущей вершины
                val maxNode = nodes.filter { it.works.contains(currentWork) && it.works.size<node.works.size }.maxBy { it.works.size }
                Log.d("GB2works","maxNode="+maxNode.works.toStr())
                exampleWorkList.add(Work("dummyWork",0,maxNode.works))
                myEdges.add(MyEdge(maxNode.works,node.works,0, exampleWorkList.last()))
                Log.d("GB22",nodes.filter { it.works.contains(currentWork) && it.works.size<node.works.size }.toString())
                thisWorks.remove(currentWork)
                thisWorks.removeAll(maxNode.works)
            }
        }

        checkForDoubleEdges()
        createGraph(myEdges)
        Log.d("GB2","${myEdges.toStr2()}")
        Log.d("GB2",checkForDoubleEdges().toString())
        //createGraph(myEdges)
    }
    fun List<Work>.toStr():String{
        return this.joinToString ( ", " ){it.name}
    }
    fun MutableList<MyEdge>.toStr2():String{
        return this.joinToString ( ", " ){it.toString()}
    }
    fun checkForDoubleEdges(){
        for(i in myEdges.indices){
            for(j in i+1 until myEdges.size){
                if(myEdges[i].dst==myEdges[j].dst && myEdges[i].src==myEdges[j].src){
                    nodes.add(Node("",works = myEdges[i].src.plus(myEdges[i].work!!).toList()))
                    myEdges.add(MyEdge(myEdges[j].src,nodes.last().works,0,
                        Work("dummyWork",0,myEdges[j].src)))
                    myEdges[j].src=nodes.last().works
                    Log.d("GB32","1111")
                }
            }
        }
    }

    private fun createGraph(edges: MutableList<MyEdge>){
        edges.forEach { edge->
            val nodesrc=nodes.find { it.works == edge.src }!!
            val nodedst=nodes.find { it.works == edge.dst }!!
            graph.addEdge(nodesrc,nodedst,edge.value)
        }
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
                    }//суда обработку двойного ребра сунуть

//                    exampleWorkList.filter { it.requiredWorks.containsAll(intersect) && it.requiredWorks.size-intersect.size>=2}.forEach { work->
//                        var amount=0
//                        work.requiredWorks.minus(intersect).forEach { remainingWork->
//
//                            if(exampleWorkList.filter { it.requiredWorks.contains(remainingWork)}.minBy { it.requiredWorks.size }==work){
//                                Log.d("GB2Ver","remaining work "+remainingWork.name)
//                                amount+=1
//                            }
//
//                        }
//                        val worksNotProcessed=work.requiredWorks.minus(intersect).toMutableList()
//                        for(i in 2..amount){
//                            exampleWorkList.add(Work("dummyWork",0,intersect.plus(worksNotProcessed[0]).toList()))
//                            nodes.add(Node("",works=intersect.plus(worksNotProcessed[0]).toList()))
//                            worksNotProcessed.removeAt(0)
//                        }
//                    }
                }
            }
        }
    }

//    private fun createGraph(edges: MutableList<Triple<Int,Int,Int>>){
//        for(i in 0..dataset.size){
//            nodes.add(Node("$i"))
//        }
//        edges.forEach{
//            graph.addEdge(nodes[it.first],nodes[it.second],it.third)
//        }
//    }
}