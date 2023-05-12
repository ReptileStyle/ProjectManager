package com.example.projectmanager.ui.optimization

import android.view.View
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
                viewModelScope.launch {
                    val graph = GraphBuilder2(state.workList)
                    val calculator =
                        GraphCalculations(myEdges = graph.myEdges, nodes = graph.myNodes)
                    val calc2 = GraphCalculations2(graphCalculations = calculator,graph)
                }
            }
            is OptimizationEvent.OnBenefitChange -> {
                state = state.copy(benefitForOneDay = event.value)
            }
        }
    }
}