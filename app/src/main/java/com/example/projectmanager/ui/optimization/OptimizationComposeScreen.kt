package com.example.projectmanager.ui.optimization

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.projectmanager.ui.optimization.component.WorkContainer1
import com.example.projectmanager.ui.renameme.Work

@Composable
fun OptimizationComposeScreen(
    viewModel: OptimizationViewModel = hiltViewModel()
) {
    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn() {
            item{
                WorkContainer1(modifier = Modifier.fillMaxWidth(), work =null)
                Divider()
            }
            items(viewModel.state.workList){
                WorkContainer1(modifier = Modifier.fillMaxWidth(), work = it)
                Divider(thickness = Dp.Hairline, color = Color.Black)
            }
        }
    }
}


