package com.example.projectmanager.ui.optimization.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog
import com.example.projectmanager.ui.renameme.Work

@Composable
fun EditWorkDialog(
    work: Work,
    onConfirm: (Work) -> Unit
) {
    var dialogState by remember {
        mutableStateOf(false)
    }
    var currentWork by remember {
        mutableStateOf(work)
    }
    if (dialogState) {
        Dialog(onDismissRequest = { dialogState = false }) {
            Column() {
                TextField(
                    value = currentWork.durationOptimistic.toString(),
                    onValueChange = { currentWork = currentWork.copy(durationOptimistic = it.toInt()) },
                    label = { Text(text = "Название") }
                )
                TextField(
                    value = currentWork.durationPessimistic.toString(),
                    onValueChange = { currentWork = currentWork.copy(durationPessimistic = it.toInt()) },
                    label = { Text(text = "Название") }
                )
                TextField(
                    value = currentWork.costToSpeedUp.toString(),
                    onValueChange = { currentWork = currentWork.copy(costToSpeedUp = it.toInt()) },
                    label = { Text(text = "Название") }
                )
            }
        }
    }
}