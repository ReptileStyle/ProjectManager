package com.example.projectmanager.ui.optimization

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OptimizationComposeScreen(
    viewModel: OptimizationViewModel = hiltViewModel()
) {
    Box(modifier = Modifier.fillMaxSize()){
        Column() {
            viewModel.state.workList.forEach {
                Text(it.name)
            }
        }
    }
}