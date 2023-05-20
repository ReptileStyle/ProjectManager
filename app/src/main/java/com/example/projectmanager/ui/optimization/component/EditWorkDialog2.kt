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
fun EditWorkDialog2(
    work: Triple<Int?, Int, Int?>,
    onConfirm: (Triple<Int?, Int, Int?>) -> Unit,
    onDismiss: () -> Unit
) {
    var currentWork by remember {
        mutableStateOf(work)
    }
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.background(color = Color.White)) {
            TextField(
                value = currentWork.first?.toString() ?: "",
                onValueChange = {
                    try {
                        currentWork = currentWork.copy(first = it.toInt())
                    } catch (e: Exception) {
                        //nothing
                    }
                },
                label = { Text(text = "Время опт.") }
            )
            TextField(
                value = currentWork.third?.toString() ?: "",
                onValueChange = {
                    try {
                        currentWork = currentWork.copy(third = it.toInt())
                    } catch (e: Exception) {
                        //nothing
                    }
                },
                label = { Text(text = "Время пес.") }
            )
            TextButton(onClick = {
                if (validateWork(currentWork)) {
                    onConfirm(currentWork)
                    onDismiss()
                } else {
                    Toast.makeText(context, "Неверное заполнение данных", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Готово")
            }
        }
    }
}

private fun validateWork(work: Triple<Int?, Int, Int?>): Boolean {
    when {
        work.first == null -> return true
        work.third == null -> return true
        work.first!! >= work.third!! -> return false
        else -> return true
    }
}