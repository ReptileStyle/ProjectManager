package com.example.projectmanager.ui.optimization

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.projectmanager.ui.optimization.component.EditWorkDialog
import com.example.projectmanager.ui.optimization.component.EditWorkDialog2
import com.example.projectmanager.ui.optimization.component.WorkContainer1
import com.example.projectmanager.ui.optimization.component.WorkContainer2
import com.example.projectmanager.ui.optimization.component.model.MonteCarloWork
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import java.text.DecimalFormat

@Composable
fun MonteCarloScreen(
    state: OptimizationState,
    onEvent: (OptimizationEvent) -> Unit
) {
    val formatter = remember {
        DecimalFormat("#,###.##")
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (lazyColumn, plot, showPlotButton) = createRefs()
        LazyColumn(modifier = Modifier
            .heightIn(max = (screenHeight * 0.75f).dp)
            .constrainAs(lazyColumn) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            item {
                WorkContainer2(
                    modifier = Modifier.fillMaxWidth(),
                    work = null,
                    monteCarloInfo = MonteCarloWork("", 0,0.0)
                )
            }
            itemsIndexed(state.workList) { index, work ->
                var dialogState by remember {
                    mutableStateOf(false)
                }
                if (dialogState) {
                    EditWorkDialog2(
                        work = state.workCostsMonteCarlo[index],
                        onConfirm = {
                            onEvent(
                                OptimizationEvent.OnEditMonteCarlo(
                                    index,
                                    it
                                )
                            )
                        },
                        onDismiss = { dialogState = false })
                }
                WorkContainer2(
                    modifier = Modifier.fillMaxWidth(),
                    work = work,
                    monteCarloInfo = state.workCostsMonteCarlo[index],
                    onClick = {dialogState=true}
                )
                Divider()
            }
        }
        if(state.monteCarloPlotInfo!=null) {
            Chart(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .constrainAs(plot) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(lazyColumn.bottom, 40.dp)
                    },
                chart = columnChart(),
                model = state.monteCarloPlotInfo,
                startAxis = startAxis(valueFormatter = { value, chartValues ->
                    formatter.format(value)
                }),
                bottomAxis = bottomAxis(
                    tickLength = 2.dp,
                )
            )
        }
        Button(
            modifier = Modifier.constrainAs(showPlotButton) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, 8.dp)
            },
            onClick = { onEvent(OptimizationEvent.OnBuildMonteCarloPlot) },
        ) {
            Text(text = "Строить гистограмму")
        }
    }
}