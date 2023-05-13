package com.example.projectmanager.ui.optimization

import com.example.projectmanager.ui.renameme.Work
import org.apache.commons.math3.fitting.WeightedObservedPoint

sealed class OptimizationEvent {
    data class OnEditWork(val index:Int,val newWork: Work):OptimizationEvent()
    object OnOptimizeButtonClick:OptimizationEvent()
    data class OnBenefitChange(val value:Int):OptimizationEvent()
    data class OnChoosePlotPoint(val point:Int):OptimizationEvent()
}