package com.example.projectmanager.ui.optimization

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.extension.setFieldValue
import kotlinx.coroutines.NonDisposableHandle.parent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlotScreen(
    state: OptimizationState,
    onEvent: (OptimizationEvent) -> Unit
) {
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .padding(top = 200.dp)) {
        val (backButton, selectButton, pickDayPager, costField, plot) = createRefs()
        Chart(
            modifier = Modifier.constrainAs(plot) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top, 40.dp)
            },
            chart = lineChart(),
            model = state.plotModel!!,
            startAxis = startAxis(valueFormatter = { value, chartValues ->
                value.toInt().toString()
            }),
            bottomAxis = bottomAxis(
                tickLength = 2.dp,
                tickPosition = HorizontalAxis.TickPosition.Center(spacing = 3),
            )
        )
        val pagerState = rememberPagerState()
        Row(
            modifier = Modifier

                .constrainAs(pickDayPager) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(plot.bottom, 16.dp)
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = Icons.Filled.KeyboardArrowLeft, contentDescription = null)
            HorizontalPager(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(45.dp),
                state = pagerState,
                pageCount = state.plotInfoList.size,
            ) {
                Text(text = "${state.plotInfoList[it].days}")
            }
            Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null)
        }

        Text(
            text = "${state.plotInfoList[pagerState.currentPage].cost}",
            modifier = Modifier.constrainAs(costField) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(pickDayPager.bottom, 16.dp)
            },
        )

        Button(
            onClick = { onEvent(OptimizationEvent.OnSelectInvestmentVariant(pagerState.currentPage)) },
            modifier = Modifier.constrainAs(selectButton) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(costField.bottom, 16.dp)
            }
        ) {
            Text(text = "Выбрать")
        }
        Button(
            onClick = { onEvent(OptimizationEvent.OnHidePlotButtonClick) },
            modifier = Modifier.constrainAs(backButton) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, 16.dp)
            }
        ) {
            Text(text = "Назад")
        }

    }
}