package com.example.projectmanager.ui.optimization

import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.ui.core.UiEvent
import com.example.projectmanager.ui.data.GraphBuilder2
import com.example.projectmanager.ui.data.GraphCalculations
import com.example.projectmanager.ui.renameme.Work
import com.example.projectmanager.ui.util.new.GraphCalculations2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


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
                val benefit = state.benefitForOneDay
                if(benefit!=null) {
                    viewModelScope.launch {
                        val graph = GraphBuilder2(state.workList)
                        val calculator =
                            GraphCalculations(myEdges = graph.myEdges, nodes = graph.myNodes)
                        val calc2 = GraphCalculations2(graphCalculations = calculator, graph)
                        val map = calc2.firstOptimization(benefit)
                        val workList = state.workList
                        Log.d("viewModel",map.toString())
                        workList.forEach {
                            val value =  map[it.name] ?: 0
                            it.invested = value.also { Log.d("viewModel",value.toString()) }
                        }
                        state = state.copy(workList = workList)
                    }
                }else{
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.Message("Заполните поле с выгодой"))
                    }
                }
            }
            is OptimizationEvent.OnBenefitChange -> {
                state = state.copy(benefitForOneDay = event.value)
            }
        }
    }
}