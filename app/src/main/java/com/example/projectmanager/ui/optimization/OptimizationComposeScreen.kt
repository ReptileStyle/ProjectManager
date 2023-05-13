package com.example.projectmanager.ui.optimization

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.projectmanager.ui.optimization.component.EditWorkDialog
import com.example.projectmanager.ui.optimization.component.WorkContainer1
import com.example.projectmanager.ui.renameme.Work

@Composable
fun OptimizationComposeScreen(
    viewModel: OptimizationViewModel = hiltViewModel()
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (lazyColumn, button, benefitField,pointField) = createRefs()
        LazyColumn(
            modifier = Modifier
                .heightIn(max = (screenHeight * 0.75f).dp)
                .constrainAs(lazyColumn) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
            item {
                WorkContainer1(modifier = Modifier.fillMaxWidth(), work = null)
                Divider()
            }
            itemsIndexed(viewModel.state.workList) { index: Int, work: Work ->
                var dialogState by remember {
                    mutableStateOf(false)
                }
                if (dialogState) {
                    EditWorkDialog(
                        work = work,
                        onConfirm = { viewModel.onEvent(OptimizationEvent.OnEditWork(index, it)) },
                        onDismiss = { dialogState = false })
                }
                WorkContainer1(
                    modifier = Modifier.fillMaxWidth(),
                    work = work,
                    onClick = { dialogState = true }
                )
                Divider(thickness = Dp.Hairline, color = Color.Black)
            }

        }
        TextField(
            value = viewModel.state.benefitForOneDay?.toString() ?: "",
            onValueChange = {
                try {
                    viewModel.onEvent(OptimizationEvent.OnBenefitChange(it.toInt()))
                } catch (e: Exception) {
                    Log.e("asd",e.stackTrace.toString())
                }
            },
            label = {
                Text(text = "Выгода за 1 день")
            },
            modifier = Modifier.constrainAs(benefitField){
                top.linkTo(lazyColumn.bottom,8.dp)
                start.linkTo(parent.start)
            }
        )
        TextField(
            value = viewModel.state.currentPoint.toString(),
            onValueChange = {
                try {
                    viewModel.onEvent(OptimizationEvent.OnChoosePlotPoint(it.toInt()))
                } catch (e: Exception) {
                    //nothing
                }
            },
            label = {
                Text(text = "Plot point")
            },
            modifier = Modifier.constrainAs(pointField){
                top.linkTo(benefitField.bottom,8.dp)
                start.linkTo(parent.start)
            }
        )
        Button(
            onClick = { viewModel.onEvent(OptimizationEvent.OnOptimizeButtonClick) },
            modifier = Modifier.constrainAs(button) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, 16.dp)
            }
        ) {
            Text(text = "Построить график оптимизации")
        }
    }
}


