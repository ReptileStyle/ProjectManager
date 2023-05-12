package com.example.projectmanager.ui.optimization

import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.projectmanager.ui.core.UiEvent
import com.example.projectmanager.ui.renameme.Work
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject


class OptimizationViewModel : ViewModel() {
    var state by mutableStateOf(OptimizationState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun setDataset(workList:List<Work>){
        state=state.copy(workList=workList)
    }


    fun onEvent(event:OptimizationEvent){

    }
}