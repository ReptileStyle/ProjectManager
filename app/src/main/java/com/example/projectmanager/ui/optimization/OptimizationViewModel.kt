package com.example.projectmanager.ui.optimization

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.ui.core.UiEvent
import com.example.projectmanager.ui.data.GraphBuilder2
import com.example.projectmanager.ui.data.GraphCalculations
import com.example.projectmanager.ui.renameme.Work
import com.example.projectmanager.ui.util.new2.GraphCalculations2
import com.example.projectmanager.ui.util.new2.PlotInfo1
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class OptimizationViewModel : ViewModel() {
    var state by mutableStateOf(OptimizationState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun setDataset(workList: List<Work>) {
        state = state.copy(workList = workList)
    }




    fun onEvent(event: OptimizationEvent) {
        when (event) {
            is OptimizationEvent.OnEditWork -> {
                val workList = state.workList.toMutableList()
                workList[event.index] = event.newWork
                state = state.copy(workList = workList)
            }
            OptimizationEvent.OnOptimizeButtonClick -> {
              optimize()
            }
            is OptimizationEvent.OnBenefitChange -> {
                state = state.copy(benefitForOneDay = event.value)
            }
            is OptimizationEvent.OnChoosePlotPoint ->{
                val map = state.plotInfoList[event.point].investmentMap
                val workList = state.workList
                Log.d("viewModel", map.toString())
                workList.forEach {
                    val value = map[it.name] ?: 0
                    it.invested = value.also { Log.d("viewModel", value.toString()) }
                }
                state = state.copy(workList = workList, currentPoint = event.point)
            }
        }
    }
    private fun optimize(){
        val benefit = state.benefitForOneDay
        if(benefit!=null) {
            viewModelScope.launch {
                val graph = GraphBuilder2(state.workList)
                val calculator =
                    GraphCalculations(myEdges = graph.myEdges, nodes = graph.myNodes)
                val calc2 = GraphCalculations2(graphCalculations = calculator, graph)
//                        val map = calc2.firstOptimization(benefit)
//                        val workList = state.workList
//                        Log.d("viewModel",map.toString())
//                        workList.forEach {
//                            val value =  map[it.name] ?: 0
//                            it.invested = value.also { Log.d("viewModel",value.toString()) }
//                        }
//                        state = state.copy(workList = workList)

                val plotList = calc2.getOptimizationGraphic(benefit)
                plotList.forEach {
                    Log.d("viewModel","${it.days} ${it.cost}")
                }
                state =state.copy(plotInfoList=plotList)
            }
        }else{
            viewModelScope.launch {
                _uiEvent.send(UiEvent.Message("Заполните поле с выгодой"))
            }
        }
    }
}