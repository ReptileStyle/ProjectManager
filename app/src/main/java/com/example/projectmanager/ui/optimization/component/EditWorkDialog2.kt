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
import com.example.projectmanager.ui.optimization.component.model.MonteCarloWork
import com.example.projectmanager.ui.renameme.Work

@Composable
fun EditWorkDialog2(
    work: MonteCarloWork,
    onConfirm: (MonteCarloWork) -> Unit,
    onDismiss: () -> Unit
) {
    var workWidth by remember {
        mutableStateOf(work.width?.toString() ?: "")
    }
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.background(color = Color.White)) {
            TextField(
                value = workWidth,
                onValueChange = {
                    workWidth = it
                },
                label = { Text(text = "Время опт.") }
            )

            TextButton(onClick = {
                try {
                    onConfirm(
                        MonteCarloWork(
                            name = work.name,
                            duration = work.duration,
                            workWidth.toDouble()
                        )
                    )
                    onDismiss()
                } catch (e: Exception) {
                    Toast.makeText(context, "Неверное заполнение данных", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Готово")
            }
        }
    }
}

