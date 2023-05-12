package com.example.projectmanager.ui.optimization.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.example.projectmanager.ui.renameme.Work

@Composable
fun EditWorkDialog(
    work: Work,
    onConfirm: (Work) -> Unit,
    onDismiss: () -> Unit
) {
    var currentWork by remember {
        mutableStateOf(work)
    }
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.background(color = Color.White)) {
            TextField(
                value = currentWork.durationOptimistic?.toString() ?: "",
                onValueChange = {
                    try {
                        currentWork = currentWork.copy(durationOptimistic = it.toInt())
                    } catch (e: Exception) {
                        //nothing
                    }
                },
                label = { Text(text = "Время мин.") }
            )
            TextField(
                value = currentWork.durationPessimistic?.toString() ?: "",
                onValueChange = {
                    try {
                        currentWork = currentWork.copy(durationPessimistic = it.toInt())
                    } catch (e: Exception) {
                        //nothing
                    }
                },
                label = { Text(text = "Время макс.") }
            )
            TextField(
                value = currentWork.costToSpeedUp?.toString() ?: "",
                onValueChange = {
                    try {
                        currentWork = currentWork.copy(costToSpeedUp = it.toInt())
                    } catch (e: Exception) {
                        //nothing
                    }
                },
                label = { Text(text = "Стоимость ускорения") }
            )
            TextButton(onClick = {
                if(validateWork(currentWork)) {
                    onConfirm(currentWork)
                    onDismiss()
                }else{
                    Toast.makeText(context,"Неверное заполнение данных",Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Готово")
            }
        }
    }
}
private fun validateWork(work: Work):Boolean{
    when{
        work.durationPessimistic==null -> return true
        work.durationOptimistic==null -> return true
        work.durationOptimistic!! >= work.durationPessimistic!! -> return false
        else-> return true
    }
}