package com.example.projectmanager.ui.optimization

import com.example.projectmanager.ui.renameme.Work
import com.example.projectmanager.ui.util.new2.PlotInfo1
import com.patrykandpatrick.vico.core.entry.ChartEntryModel

data class OptimizationState(
    val workList:List<Work> = listOf(),
    val benefitForOneDay:Int? = null,
    val plotInfoList:List<PlotInfo1> = listOf(),
    val selectedVariant:Int? =null,
    val plotModel:ChartEntryModel? = null,
    val isPlotVisible:Boolean = false
)