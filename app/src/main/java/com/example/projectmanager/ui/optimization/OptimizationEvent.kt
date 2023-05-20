package com.example.projectmanager.ui.optimization

import com.example.projectmanager.ui.optimization.component.model.MonteCarloWork
import com.example.projectmanager.ui.renameme.Work
import org.apache.commons.math3.fitting.WeightedObservedPoint

sealed class OptimizationEvent {
    data class OnEditWork(val index:Int,val newWork: Work):OptimizationEvent()
    object OnOptimizeButtonClick:OptimizationEvent()
    data class OnBenefitChange(val value:Int):OptimizationEvent()
    object OnHidePlotButtonClick:OptimizationEvent()
    object OnShowPlotButtonClick:OptimizationEvent()

    data class OnSelectInvestmentVariant(val index:Int):OptimizationEvent()

    object OnChooseMonteCarloMode:OptimizationEvent()

    object OnBuildMonteCarloPlot:OptimizationEvent()

    data class OnEditMonteCarlo(val index: Int,val value:MonteCarloWork):OptimizationEvent()

}