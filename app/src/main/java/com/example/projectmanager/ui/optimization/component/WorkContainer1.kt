package com.example.projectmanager.ui.optimization.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.projectmanager.ui.renameme.Work

@Composable
fun WorkContainer1(
    modifier: Modifier,
    work: Work?
) {
    if(work!=null) {
        Row(modifier) {
            Text(text = work.name, modifier = Modifier.weight(2f))
            Text(text = work.durationOptimistic?.toString() ?: "", modifier = Modifier.weight(1f))
            Text(text = work.durationPessimistic?.toString() ?: "", modifier = Modifier.weight(1f))
            Text(
                text = work.requiredWorks.joinToString(separator = ",") { it.name },
                modifier = Modifier.weight(2.5f)
            )
            Text(text = work.costToSpeedUp?.toString() ?: "", modifier = Modifier.weight(1f))
        }
    }else{
        Row(modifier) {
            Text(text = "Название", modifier = Modifier.weight(2f))
            Text(text = "Время мин.", modifier = Modifier.weight(1f))
            Text(text ="Время макс.", modifier = Modifier.weight(1f))
            Text(
                text = "Требуемые работы",
                modifier = Modifier.weight(2.5f)
            )
            Text(text = "ускор.", modifier = Modifier.weight(1f))
        }
    }
}