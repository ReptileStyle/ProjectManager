package com.example.projectmanager.ui.optimization

import com.example.projectmanager.ui.renameme.Work
import com.example.projectmanager.ui.util.new2.PlotInfo1

data class OptimizationState(
    val workList:List<Work> = listOf(),
    val benefitForOneDay:Int? = null,
    val currentPoint:Int = 0,
    val plotInfoList:List<PlotInfo1> = listOf<PlotInfo1>()
)